package agh.dp.facade;

import agh.dp.database.PermissionRepository;
import agh.dp.database.RoleRepository;
import agh.dp.database.UserRepository;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.RoleWithPermissions;
import agh.dp.models.User;

public class RoleWithPermissionsFacade {

    public static final RoleWithPermissionsFacade INSTANCE = new RoleWithPermissionsFacade();

    UserRepository userRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;

    private RoleWithPermissionsFacade() {}

    public void setPermissionRepository(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
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
            //TODO: obsługa wyjątków
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
