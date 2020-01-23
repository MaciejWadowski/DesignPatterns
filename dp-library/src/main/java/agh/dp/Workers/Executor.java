package agh.dp.Workers;

import agh.dp.database.*;
import agh.dp.facade.RoleWithPermissionsFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.User;
import agh.dp.providers.PermissionsProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Executor {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public Executor(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getUserPermissions(String userName, List<String> tableNames, int accessLevel){
        List<Long> userRoleIds = userRoleIds(userName);

        PermissionServiceMap permissionServiceMap = new PermissionServiceMap(permissionRepository);

        List<Permission> usersPermissions = new ArrayList<>();
        for (String tableName : tableNames) {
            usersPermissions.addAll(permissionServiceMap.findAllByRoleIdsAndTableNameAndAccessLevel(userRoleIds, tableName, accessLevel));
        }
        return usersPermissions;
    }

//    public boolean hasUserInsertPermission(String username, String tableName){
//        List<Long> userRoleIds = userRoleIds(username);
//
//        PermissionServiceMap permissionServiceMap = new PermissionServiceMap(permissionRepository);
//
//        List<Permission> usersPermissions = permissionServiceMap.findAllByRoleIdsAndTableNameAndAccessLevel(userRoleIds, tableName,PermissionsProvider.INSERT);
//        if (!usersPermissions.isEmpty()){
//            return true;
//        } else return false;
//    }

    private List<Long> userRoleIds(String userName){
        UserServiceMap userServiceMap = new UserServiceMap(userRepository);
        User user = userServiceMap.findByUserName(userName);

        List<Long> userRoleIds = new ArrayList<>();

        Role role = roleRepository.findById(user.getRoleId()).isPresent() ?
                roleRepository.findById(user.getRoleId()).get() : null;
        userRoleIds.add(role.getId());

        while (role != null && role.getInheritedRoleId() != null){
            role = roleRepository.findById(role.getInheritedRoleId()).isPresent() ?
                    roleRepository.findById(role.getInheritedRoleId()).get() : null;
            userRoleIds.add(role.getId());
        }
        return userRoleIds;
    }

}
