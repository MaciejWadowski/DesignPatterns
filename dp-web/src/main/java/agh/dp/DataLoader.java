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

    public DataLoader(SafetyModuleFacade facade) {
        this.facade = facade;
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
                .setInheritedRole(roleWithPermissionsPrimary.getRole().getId())
                .build();
        facade.saveRoleWithPermissions(roleWithPermissions);

        facade.assignUserToRole("user", roleWithPermissionsPrimary);
        facade.assignUserToRole("user2", roleWithPermissions);
    }
}
