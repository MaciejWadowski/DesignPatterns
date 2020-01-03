package agh.dp;

import agh.dp.database.PermissionRepository;
import agh.dp.database.RoleRepository;
import agh.dp.facade.RoleWithPermissionsFacade;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.providers.PermissionsProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
@SpringBootTest
class WebApplicationTests {

    @Mock
    StudentRepository studentRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PermissionRepository permissionRepository;

    @Test
    void contextLoads() {
        Student student = new Student();
        student.setFirstName("Maciek");
        studentRepository.save(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        assertEquals(student.getFirstName(), "Maciek");
       // verify(studentRepository).save(any(Student.class));
    }

    @Test
    void findByIdTest() {
        Student student = new Student();
        student.setFirstName("Maciek");
        Optional<Student> studentOpt = Optional.of(student);
        when(studentRepository.findById(any())).thenReturn(studentOpt);
    //    verify(studentRepository).findById(anyLong());
    }

    @Test
    void buildRoleTest() {
        RoleWithPermissions roleWithPermissions = new RoleWithPermissions.RoleWithPermissionsBuilder("Prezes")
                .setInheritedRole(2L)
                .addPermissions("Zarobki",
                        PermissionsProvider.DELETE + PermissionsProvider.READ,
                        12, 23, 34)
                .addPermissions("InnaTabelka",
                        PermissionsProvider.UPDATE + PermissionsProvider.READ,
                        10)
                .addInsertPermissions("Zarobki", "InnaTabelka")
                .build();

        System.out.println("Role builded:" + roleWithPermissions.getRoleName());

        roleWithPermissions.assignUserToRole("KowalskiJan");
    }

}