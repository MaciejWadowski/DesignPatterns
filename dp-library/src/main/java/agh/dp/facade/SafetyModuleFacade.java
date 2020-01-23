package agh.dp.facade;

import agh.dp.database.PermissionRepository;
import agh.dp.database.RoleRepository;
import agh.dp.database.UserRepository;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.models.User;
import org.springframework.stereotype.Component;

@Component
public class SafetyModuleFacade {

    UserRepository userRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;

    public SafetyModuleFacade(UserRepository userRepository, PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public PermissionRepository getPermissionRepository(){
        return  this.permissionRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void saveRoleWithPermissions(RoleWithPermissions roleWithPermissions){
        if (roleRepository == null || permissionRepository == null){
            return;
        }

        Role savedRole = roleRepository.save(roleWithPermissions.getRole());

        if (savedRole != null) {
            for (Permission permission : roleWithPermissions.getPermissions()) {
                permission.setRoleId(savedRole.getId());
                permissionRepository.save(permission);
            }
        }
    }

    public void assignUserToRole(String username, RoleWithPermissions roleWithPermissions){
        if (roleWithPermissions.getRole() != null && roleWithPermissions.getRole().getId() != 0){
            User user = new User(username, roleWithPermissions.getRole().getId());
            userRepository.save(user);
        }
    }
}
