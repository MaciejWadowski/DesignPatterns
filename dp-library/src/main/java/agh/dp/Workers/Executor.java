package agh.dp.Workers;

import agh.dp.database.*;
import agh.dp.models.Permission;
import agh.dp.models.QueryToInject;
import agh.dp.models.Role;
import agh.dp.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Executor {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    QueryToInjectRepository queryToInjectRepository;

    public Executor(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, QueryToInjectRepository queryToInjectRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.queryToInjectRepository = queryToInjectRepository;
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

    public boolean shouldQueryBeInjected(String tableName, int operationLevel){
        QueryToInjectServiceMap queryToInjectServiceMap = new QueryToInjectServiceMap(queryToInjectRepository);
        QueryToInject queryToInject = queryToInjectServiceMap.getQueryToInjectByTableNameAndAccessLevel(tableName, operationLevel);
        if (queryToInject == null) return false; return true;
    }

}
