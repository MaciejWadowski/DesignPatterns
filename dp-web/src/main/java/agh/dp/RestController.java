package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.facade.DatabaseOperations;
import agh.dp.facade.SafetyModuleFacade;
import agh.dp.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private Session session;
    private DatabaseOperations db;
    private Executor executor;

    public RestController(Executor executor) {
        this.executor = executor;
    }
    private void updateSession() {
        Interceptor interceptor = new Interceptor(executor);
        this.session = HibernateUtil.getSessionFactory("org.h2.Driver", "jdbc:h2:mem:testdb", "sa", "", Student.class, Przedmiot.class)
                .withOptions()
                .interceptor(interceptor)
                .openSession();
        db = new DatabaseOperations(session);
    }

    @DeleteMapping("/deleteStudent/{id}")
    private Boolean deleteStudent(@PathVariable Long id) {
        updateSession();
        return db.delete(new Student(id, null, null), Student.class, id);
    }

    @GetMapping("/showStudent/{id}")
    private Student showStudent(@PathVariable Long id) {
        updateSession();
        return (Student) db.get(Student.class, id);
    }

    @PutMapping("/updateStudent/{id}")
    private void updateStudent(@PathVariable Long id) {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        Student student = new Student(id, generatedString, generatedString);
        student.setId(id);
        updateSession();
        try {
            db.update(student);
        } catch (Exception e) {

        }
    }

    @PostMapping("/createStudent")
    private Long createStudent() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        Student student = new Student(generatedString, generatedString);
        updateSession();
        Long id2 = db.save(student);
        student.setId(id2);
        return id2;
    }
    @DeleteMapping("/deletePrzedmiot/{id}")
    private Boolean deletePrzedmiot(@PathVariable Long id) {
        updateSession();
        return db.delete(new Przedmiot(id, null), Student.class, id);
    }

    @GetMapping("/showPrzedmiot/{id}")
    private Przedmiot showPrzedmiot(@PathVariable Long id) {
        updateSession();
        return (Przedmiot) db.get(Przedmiot.class, id);

    }

    @PutMapping("/updatePrzedmiot/{id}")
    private Boolean updatePrzedmiot(@PathVariable Long id) {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        Przedmiot przedmiot = new Przedmiot(id, generatedString);
        updateSession();
        przedmiot.setId(id);
        return db.update(przedmiot);
    }

    @PostMapping("/createPrzedmiot")
    private Przedmiot createPrzedmiot() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        Przedmiot przedmiot = new Przedmiot(generatedString);
        updateSession();
        Long id = (Long) db.save(przedmiot);
        przedmiot.setId(id);
        return  przedmiot;
    }
}
