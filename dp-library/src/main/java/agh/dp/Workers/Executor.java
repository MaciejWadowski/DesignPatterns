package agh.dp.Workers;

import agh.dp.database.*;
import agh.dp.facade.RoleWithPermissionsFacade;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.User;

import java.util.ArrayList;
import java.util.List;

public class Executor {
    private static UserRepository userRepository = RoleWithPermissionsFacade.INSTANCE.getUserRepository();
    private static RoleRepository roleRepository = RoleWithPermissionsFacade.INSTANCE.getRoleRepository();
    private static PermissionRepository permissionRepository = RoleWithPermissionsFacade.INSTANCE.getPermissionRepository();

    public static List<Permission> getUserPermissions(String userName, List<String> tableNames, int accessLevel){
        UserServiceMap userServiceMap = new UserServiceMap(userRepository);
        User user = userServiceMap.findByUserName(userName);

        List<Long> userRoleIds = new ArrayList<>();

        Role role = roleRepository.findById(user.getRoleId()).isPresent() ?
                roleRepository.findById(user.getRoleId()).get() : null;

        while (role != null && role.getInheritedRoleId() != null){
            userRoleIds.add(role.getId());
            role = roleRepository.findById(role.getInheritedRoleId()).isPresent() ?
                    roleRepository.findById(role.getInheritedRoleId()).get() : null;
        }

        PermissionServiceMap permissionServiceMap = new PermissionServiceMap(permissionRepository);

        List<Permission> usersPermissions = new ArrayList<>();
        for (String tableName : tableNames) {
            usersPermissions.addAll(permissionServiceMap.findAllByRoleIdsAndTableNameAndAccessLevel(userRoleIds, tableName, accessLevel));
        }
        return usersPermissions;
    }

}
