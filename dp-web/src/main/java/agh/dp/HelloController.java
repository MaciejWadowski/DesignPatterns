package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.facade.DatabaseOperations;
import agh.dp.facade.SafetyModuleFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
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
    private Executor executor;

    public HelloController(SafetyModuleFacade safetyModuleFacade, Executor executor) {
        this.facade = safetyModuleFacade;
        this.executor = executor;
    }

    @GetMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        Interceptor interceptor = new Interceptor(executor);
        this.session = HibernateUtil.getSessionFactory("org.h2.Driver", "jdbc:h2:mem:testdb", "sa", "", Student.class)
                .withOptions()
                .interceptor(interceptor)
                .openSession();
        db = new DatabaseOperations(session);
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
        String error = "";
        if (buttonClicked != null) {
            if ("".equals(id)){
                updated = false;
                error = "Id cannot be empty.";
            }
            else {
                error = "";
                if (buttonClicked.equals("addStudent")) {
                    val = db.save(new Student(Long.parseLong(id), name, lastName));
                    if (val != null) {
                        updated = true;
                    }
                } else if (buttonClicked.equals("removeStudent")) {
                    updated = db.delete(new Student(Long.parseLong(id), null, null), Student.class, Long.parseLong(id));
                } else if (buttonClicked.equals("updateStudent")) {
                    Student student2 = (Student) db.load(Student.class, Long.parseLong(id));
                    if (student2 != null) {
                        student2.setLastName(lastName);
                        student2.setFirstName(name);
                        updated = db.update(student2);
                    } else updated = false;
                } else if (buttonClicked.equals("showStudent")) {
                    student = (Student) db.get(Student.class, Long.parseLong(id));
                    if (student != null) {
                        updated = true;
                    }
                }
            }
        }
        ModelAndView model = new ModelAndView("hello");
        model.addObject("result", student);
        model.addObject("success", updated);
        model.addObject("save", val);
        model.addObject("error",error);
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
