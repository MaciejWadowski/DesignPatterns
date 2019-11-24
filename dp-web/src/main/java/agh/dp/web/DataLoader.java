package agh.dp.web;

import agh.dp.lib.library.aspect.Employee;
import agh.dp.lib.library.aspect.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;


    public DataLoader(StudentRepository studentRepository, EmployeeRepository employeeRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Student student = new Student();
        student.setFirstName("Maciek");
        student.setLastName("XDDD");
        studentRepository.save(student);
        Optional<Student> stud = studentRepository.findById(1L);

        Employee employee = new Employee();
        employee.setFirstName("XD");
        employee.setLastName("XDASDA");
        employeeRepository.save(employee);
    }
}
