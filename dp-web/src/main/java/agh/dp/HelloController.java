package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.facade.DatabaseOperations;
import agh.dp.facade.SafetyModuleFacade;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    private SafetyModuleFacade facade;
    private Session session;
    private DatabaseOperations db;

    public HelloController(SafetyModuleFacade safetyModuleFacade, Executor executor) {
        this.facade = safetyModuleFacade;
        Interceptor interceptor = new Interceptor(executor);
        this.session = HibernateUtil.getSessionFactory("org.h2.Driver", "jdbc:h2:mem:testdb", "sa", "", User.class, Student.class)
                .withOptions()
                .interceptor(interceptor)
                .openSession();
        db = new DatabaseOperations(session);
    }

    @GetMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        RoleWithPermissions roleWithPermissionsPrimary = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedziczona")
                .addInsertPermissions("Student")
                .addPermissions("Student", PermissionsProvider.READ, 1,2,3)
                .addPermissions("Student", PermissionsProvider.UPDATE, 1,4)
                .addPermissions("Student", PermissionsProvider.DELETE, 3)
                .build();
        facade.saveRoleWithPermissions(roleWithPermissionsPrimary);

        RoleWithPermissions roleWithPermissions = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedzicząca")
                .addPermissions("Student", PermissionsProvider.READ, 4)
                .addPermissions("Student", PermissionsProvider.UPDATE, 3)
                .setInheritedRole(roleWithPermissionsPrimary.getRole().getId())
                .build();
        facade.saveRoleWithPermissions(roleWithPermissions);

        Iterable<Role> roles = facade.getRoleRepository().findAll();
        Iterable<Permission> permissions = facade.getPermissionRepository().findAll();
        facade.assignUserToRole(getCurrentUsername(), roleWithPermissions);
        return "hello";
    }

    @PostMapping(value="/hello")
    public ModelAndView onClickAction(HttpServletRequest request) {
        String buttonClicked = request.getParameter("button");
        String id = request.getParameter("id");
        String name = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        System.out.println("id = " + id + ";name = " + name + ";lastName = " + lastName);
        Student student = null;
        Boolean updated = true;
        Long val = null;
        if (buttonClicked != null) {
            if (buttonClicked.equals("addStudent")) {
                val = db.save(new Student(Long.parseLong(id), name, lastName));
                if (val != null) {
                    updated = true;
                }
            } else if (buttonClicked.equals("removeStudent")) {
                updated = db.delete(new Student(Long.parseLong(id), null, null), Student.class, Long.parseLong(id));
            } else if (buttonClicked.equals("updateStudent")) {
                Student student2 = session.load(Student.class, Long.parseLong(id));
                if (student2 == null) {
                    student2 = new Student(Long.parseLong(id), name, lastName);
                }
                student2.setLastName(lastName);
                student2.setFirstName(name);
                updated =  db.update(student2);
            } else {
               student =  (Student) db.get(Student.class, Long.parseLong(id));
               if (student != null) {
                   updated = true;
               }
            }
        }
        ModelAndView model = new ModelAndView("hello");
        model.addObject("result", student);
        model.addObject("success", updated);
        model.addObject("save", val);
        return model;
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
