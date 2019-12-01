package agh.dp.database;

import agh.dp.Logging;
import agh.dp.models.Role;
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

    Role role;

    @BeforeEach
    void setUp() {
        role = new Role("Ksiegowy", "");
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
        roles.add(new Role("Ksiegowy", ""));
        roles.add(new Role("Prezes", "Ksiegowy"));
        roles.add(new Role("Maciek", "Ksiegowy"));
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
        roles.add(new Role("Ksiegowy", ""));
        roles.add(new Role("Prezes", "Ksiegowy"));
        roles.add(new Role("Maciek", "Ksiegowy"));
        //when
        when(roleRepository.saveAll(anySet())).thenReturn(roles);
        Set<Role> rolesInRepo = new HashSet<>((Collection) roleRepository.saveAll(roles));
        //then
        assertEquals(rolesInRepo, roles);
        verify(roleRepository).saveAll(anySet());
    }
}