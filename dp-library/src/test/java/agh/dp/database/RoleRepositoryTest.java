package agh.dp.database;

import agh.dp.Logging;
import agh.dp.facade.RoleWithPermissionsFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.providers.PermissionsProvider;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//something doesn't work, please insert class names which you use
@SpringBootTest(classes = {RoleRepository.class, Logging.class})
class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;
    @Mock
    PermissionRepository permissionRepository;

    Role role;

    @BeforeEach
    void setUp() {
        role = new Role("Ksiegowy", 2L);
        role.setId(1L);
    }

    @Test
    public void shouldFindByIdReturnSameObject() {
        //given
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        Optional<Role> foundRole = roleRepository.findById(1L);

        //then
        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());
        verify(roleRepository).findById(anyLong());
    }

    @Test
    public void shouldSaveRoleObject() {
        //given
        //when
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        Role savedRole = roleRepository.save(role);

        //then
        assertEquals(savedRole, role);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void shouldRetrieveAllRoles() {
        //given
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Ksiegowy", 2L));
        roles.add(new Role("Prezes", 3L));
        roles.add(new Role("Maciek", 3L));
        //when
        when(roleRepository.findAll()).thenReturn(roles);
        Set<Role> rolesInRepo = new HashSet<>((Collection)roleRepository.findAll());
        //then
        assertEquals(roles, rolesInRepo);
        verify(roleRepository).findAll();
    }

    @Test
    public void shouldSaveAllValues() {
        //given
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Ksiegowy", 2L));
        roles.add(new Role("Prezes", 3L));
        roles.add(new Role("Maciek", 3L));
        //when
        when(roleRepository.saveAll(anySet())).thenReturn(roles);
        Set<Role> rolesInRepo = new HashSet<>((Collection) roleRepository.saveAll(roles));
        //then
        assertEquals(rolesInRepo, roles);
        verify(roleRepository).saveAll(anySet());
    }

    @Test
    public void shouldSaveRoleAndPermissions() {
        RoleWithPermissions roleWithPermissions = new RoleWithPermissions
                .RoleWithPermissionsBuilder("NazwaRoli")
                .setInheritedRole(1L)
                .addInsertPermissions("nazwaTabeli1", "nazwaTabeli2")
                .addPermissions("nazwaTabeli1",
                        PermissionsProvider.DELETE + PermissionsProvider.READ,
                        1, 13, 32)
                .addPermissions("nazwaTabeli2",
                        PermissionsProvider.UPDATE + PermissionsProvider.READ,
                        134, 23, 65, 23, 4)
                .build();

        roleWithPermissions.getRole().setId(1L);
        long idCounter = 0L;
        for (Permission permission : roleWithPermissions.getPermissions()){
            permission.setId(1L + idCounter);
            idCounter += 1L;
        }

        RoleWithPermissionsFacade facade = RoleWithPermissionsFacade.INSTANCE;
        facade.setRoleRepository(roleRepository);
        facade.setPermissionRepository(permissionRepository);
        facade.saveRoleWithPermissions(roleWithPermissions);

    }
}