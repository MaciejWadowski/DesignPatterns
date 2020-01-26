package agh.dp.database;

import agh.dp.facade.SafetyModuleFacade;
import agh.dp.models.RoleWithPermissions;
import agh.dp.providers.PermissionsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleTests {

    private SafetyModuleFacade facade;
    @Mock
    UserRepository userRepository;
    @Mock
    PermissionRepository permissionRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    QueryToInjectRepository queryToInjectRepository;


    @BeforeEach
    public void setUp(){
        facade = new SafetyModuleFacade(userRepository,permissionRepository,roleRepository, queryToInjectRepository);
    }

    @Test
    public void testCreatingRolesWithPermissions(){
        RoleWithPermissions roleWithPermissionsPrimary = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedziczona")
                .addInsertPermissions("Student")
                .addPermissions("Student", PermissionsProvider.READ, 1,2,3)
                .addPermissions("Student", PermissionsProvider.UPDATE, 1,4)
                .addPermissions("Student", PermissionsProvider.DELETE, 3)
                .build();
        roleWithPermissionsPrimary.getRole().setId((long) 1);

        when(roleRepository.save(any())).thenReturn(roleWithPermissionsPrimary.getRole());

        facade.saveRoleWithPermissions(roleWithPermissionsPrimary);

        RoleWithPermissions roleWithPermissions = new RoleWithPermissions.RoleWithPermissionsBuilder("dziedzicząca")
                .addPermissions("Student", PermissionsProvider.READ, 4)
                .addPermissions("Student", PermissionsProvider.UPDATE, 3)
                .setInheritedRole(roleWithPermissionsPrimary.getRole().getId())
                .build();
        roleWithPermissions.getRole().setId((long) 2);

        when(roleRepository.save(any())).thenReturn(roleWithPermissions.getRole());

        facade.saveRoleWithPermissions(roleWithPermissions);
        facade.assignUserToRole("user1",roleWithPermissions);
        facade.assignUserToRole("user2Primary",roleWithPermissionsPrimary);

        assertTrue(roleWithPermissionsPrimary.getRoleName().equals("dziedziczona"));
        assertEquals("dziedzicząca", roleWithPermissions.getRoleName());
        assertEquals(roleWithPermissions.getInheritedRoleId(), roleWithPermissionsPrimary.getRole().getId());
    }

}