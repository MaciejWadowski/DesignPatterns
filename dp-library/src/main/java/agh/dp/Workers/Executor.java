package agh.dp.Workers;

import agh.dp.database.*;
import agh.dp.models.Permission;
import agh.dp.models.Role;
import agh.dp.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Executor {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

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

    public boolean hasUserPermissionToRecord(String userName, String tableName, int accessLevel, Long recordId){
        List<String> tableNames = new ArrayList<>();
        tableNames.add(tableName);
        List<Permission> allUsersPermissions = getUserPermissions(userName, tableNames, accessLevel);
        for(Permission permission : allUsersPermissions){
            if (permission.getRecordId().equals(recordId)){
                return true;
            }
        }
        return false;
    }

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
