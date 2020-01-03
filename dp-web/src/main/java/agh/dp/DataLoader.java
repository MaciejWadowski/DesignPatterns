package agh.dp;

import agh.dp.database.PermissionRepository;
import agh.dp.database.RoleRepository;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.providers.PermissionsProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public DataLoader(StudentRepository studentRepository, PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.studentRepository = studentRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        Student student = new Student();
//        student.setFirstName("Maciek");
//        student.setLastName("XDDD");
//        studentRepository.save(student);
//        Optional<Student> stud = studentRepository.findById(1L);
//        System.out.println(stud.toString());
//
//        // zawsze jak obiekt z repo zawiera sie w innym, to wpierw zapisujemy jego komponent do bazy!!!
//        // bo moze sie przewrocic
//        Role role = new Role();
//        role.setInheritedRoleName("");
//        role.setRoleName("GameMaster");
//        Role savedRole = roleRepository.save(role);
//
//        Permission permission = new Permission();
//        permission.setRoleId(savedRole.getId());
//        permission.setAccessLevel(PermissionsProvider.INSERT);
//        permission.setTableName("some table");
//        Permission permission1 = permissionRepository.save(permission);
//        System.out.println(permission1);
//        Optional<Permission> permission2 = permissionRepository.findById(permission1.getId());
    }
}
