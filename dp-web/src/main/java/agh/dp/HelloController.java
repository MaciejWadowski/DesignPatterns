package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.database.UserRepository;
import agh.dp.facade.RoleWithPermissionsFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.models.User;
import agh.dp.providers.PermissionsProvider;
import agh.dp.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HelloController {

    private RoleWithPermissionsFacade facade;

    private Session session;

    public HelloController(RoleWithPermissionsFacade roleWithPermissionsFacade, Executor executor) {
        this.facade = roleWithPermissionsFacade;
        this.session = HibernateUtil.getSessionFactory("org.h2.Driver", "jdbc:h2:mem:testdb", "sa", "", User.class, Student.class)
                .withOptions()
                .interceptor(new Logging(executor))
                .openSession();
    }

    @GetMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        RoleWithPermissions roleWithPermissions = new RoleWithPermissions.RoleWithPermissionsBuilder("nazwa")
                .addPermissions("Student", PermissionsProvider.READ, 1,2,3,4)
                .addPermissions("Student", PermissionsProvider.UPDATE, 3)
                .build();
        facade.saveRoleWithPermissions(roleWithPermissions);
        Iterable<Role> roles = facade.getRoleRepository().findAll();
        Iterable<Permission> permissions = facade.getPermissionRepository().findAll();
        facade.assignUserToRole(getCurrentUsername(), roleWithPermissions);
        Iterable<User> users = facade.getUserRepository().findAll();

        //session.save(new Student("maciek", "jakis"));
        //System.out.println("student saved");
        Student sampleStudent = session.get(Student.class, 1L);
        List<Student> allStudents = HibernateUtil.loadAllData(Student.class, session);
        return "hello";
    }

    @PostMapping(value="/hello")
    public String onClickAction(HttpServletRequest request) {
        String buttonClicked = request.getParameter("button");
        String id = request.getParameter("id");
        String name = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        System.out.println("id = " + id + ";name = " + name + ";lastName = " + lastName);
        if (buttonClicked != null) {
            if (buttonClicked.equals("addStudent")) {
                org.hibernate.Transaction tr = session.beginTransaction();
                session.save(new Student(Long.parseLong(id), name, lastName));
                tr.commit();
            } else if (buttonClicked.equals("removeStudent")) {
                org.hibernate.Transaction tr = session.beginTransaction();
                Student student =  session.load(Student.class, Long.parseLong(id));
                session.delete(student);
                tr.commit();
            } else if (buttonClicked.equals("updateStudent")) {
                //TODO trza naprawic
                org.hibernate.Transaction tr = session.beginTransaction();
                session.update(new Student(Long.parseLong(id), name, lastName));
                tr.commit();
            } else {

                session.get(Student.class, Long.parseLong(id));
            }
        }
        return "hello";
    }

    private String getCurrentUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
