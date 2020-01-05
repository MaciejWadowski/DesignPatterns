package agh.dp;

import agh.dp.database.UserRepository;
import agh.dp.models.User;
import agh.dp.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    private final UserRepository repository;

    private final Session session = HibernateUtil.getSessionFactory("org.h2.Driver", "jdbc:h2:mem:testdb", "sa", "", User.class, Student.class)
            .withOptions()
            .interceptor(new Logging())
            .openSession();

    public HelloController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        session.save(new User("username", 2L));
        System.out.println("boop");
        session.get(User.class, 1L);

        session.save(new Student(1L, "maciek", "jakis"));
        System.out.println("student saved");
        session.get(Student.class, 1L);
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
}
