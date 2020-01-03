package agh.dp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    private final StudentRepository repository;

    public HelloController(StudentRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = {"hello", "/hello", "hello.html"})
    public String bugHandler() {
        repository.save(new Student());
        return "hello";
    }
}
