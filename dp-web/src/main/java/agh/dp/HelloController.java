package agh.dp;

import agh.dp.database.UserRepository;
import agh.dp.models.User;
import agh.dp.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    private final UserRepository repository;

    public HelloController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        Session session = HibernateUtil.getSessionFactory()
                .withOptions()
                .interceptor(new Logging())
                .openSession();

        session.save(new User("username", 2L));
        System.out.println("boop");
        session.get(User.class, 1L);
        return "hello";
    }
}
