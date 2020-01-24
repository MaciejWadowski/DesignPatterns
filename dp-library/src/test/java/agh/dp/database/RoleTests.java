package agh.dp.database;

import agh.dp.Workers.Executor;
import agh.dp.facade.DatabaseOperations;
import agh.dp.facade.SafetyModuleFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.models.User;
import agh.dp.providers.PermissionsProvider;
import agh.dp.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@DataJpaTest
class RoleTests {

    private SafetyModuleFacade facade;
    @Mock
    UserRepository userRepository;
    @Mock
    PermissionRepository permissionRepository;
    @Mock
    RoleRepository roleRepository;


    @BeforeEach
    public void setUp(){
        facade = new SafetyModuleFacade(userRepository,permissionRepository,roleRepository);
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
        //assertEquals(roleWithPermissions.getInheritedRoleId(), roleWithPermissionsPrimary.getRole().getId());
        //assertOtherThings?
        roleRepository.findAll();
    }

}