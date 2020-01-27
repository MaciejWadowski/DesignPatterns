package agh.dp;

import agh.dp.database.PermissionRepository;
import agh.dp.database.RoleRepository;
import agh.dp.facade.SafetyModuleFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.providers.PermissionsProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final SafetyModuleFacade facade;
    private final PrzedmiotRepository przedmiotRepository;
    private final StudentRepository studentRepository;

    public DataLoader(SafetyModuleFacade facade, PrzedmiotRepository przedmiotRepository, StudentRepository studentRepository) {
        this.facade = facade;
        this.przedmiotRepository = przedmiotRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        RoleWithPermissions roleWithPermissionsPrimary = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedziczona")
                .addPermissions("Student", PermissionsProvider.READ, 1)
                //.addPermissions("Student", PermissionsProvider.UPDATE, 1)
                //.addPermissions("Student", PermissionsProvider.DELETE, 1)
                .build();
        facade.saveRoleWithPermissions(roleWithPermissionsPrimary);

        RoleWithPermissions roleWithPermissions = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedzicząca")
                .addInsertPermissions("Student")
                .addPermissions("Student", PermissionsProvider.READ, 2,3,4)
                .addPermissions("Student", PermissionsProvider.UPDATE, 2,3,4)
                .addPermissions("Student", PermissionsProvider.DELETE, 2,3)
                .addPermissions("Przedmiot", PermissionsProvider.READ, 1,2,3)
                .addInsertPermissions("Przedmiot")
                .setInheritedRole(roleWithPermissionsPrimary.getRole().getId())
                .build();
        facade.saveRoleWithPermissions(roleWithPermissions);

        facade.assignUserToRole("user", roleWithPermissionsPrimary);
        facade.assignUserToRole("user2", roleWithPermissions);

        facade.setQueryToInject("Student", PermissionsProvider.UPDATE, PermissionsProvider.READ);

        przedmiotRepository.save(new Przedmiot("przedmiot1"));
        przedmiotRepository.save(new Przedmiot("przedmiot2"));
        przedmiotRepository.save(new Przedmiot("przedmiot3"));
        przedmiotRepository.save(new Przedmiot("przedmiot4"));
        przedmiotRepository.save(new Przedmiot("przedmiot5"));

        studentRepository.save(new Student("some1", "dude1"));
        studentRepository.save(new Student("some2", "dude2"));
        studentRepository.save(new Student("some3", "dude3"));
        studentRepository.save(new Student("some4", "dude4"));
        studentRepository.save(new Student("some5", "dude5"));
    }
}
