package agh.dp.facade;

import agh.dp.database.PermissionRepository;
import agh.dp.database.QueryToInjectRepository;
import agh.dp.database.RoleRepository;
import agh.dp.database.UserRepository;
import agh.dp.models.*;
import agh.dp.providers.PermissionsProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SafetyModuleFacade {

    //private final RoleWithPermissionsFacade INSTANCE = new RoleWithPermissionsFacade();

    UserRepository userRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    QueryToInjectRepository queryToInjectRepository;

    public SafetyModuleFacade(UserRepository userRepository, PermissionRepository permissionRepository, RoleRepository roleRepository, QueryToInjectRepository queryToInjectRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.queryToInjectRepository = queryToInjectRepository;
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

    public void setQueryToInject(String tableName, int ... permissionLevel){
        if (queryToInjectRepository == null) return;
        List<QueryToInject> queriesToInject = new ArrayList<>();
        for (int level : permissionLevel) {
            switch (level) {
                case PermissionsProvider.DELETE:
                    queriesToInject.add(new QueryToInject(PermissionsProvider.DELETE, PermissionsProvider.DELETE_NAME, tableName));
                    break;
                case PermissionsProvider.INSERT:
                    queriesToInject.add(new QueryToInject(PermissionsProvider.INSERT, PermissionsProvider.INSERT_NAME, tableName));
                    break;
                case PermissionsProvider.READ:
                    queriesToInject.add(new QueryToInject(PermissionsProvider.READ, PermissionsProvider.READ_NAME, tableName));
                    break;
                case PermissionsProvider.UPDATE:
                    queriesToInject.add(new QueryToInject(PermissionsProvider.UPDATE, PermissionsProvider.UPDATE_NAME, tableName));
                    break;
            }
        }
        if (!queriesToInject.isEmpty()) {
            for (QueryToInject query : queriesToInject){
                queryToInjectRepository.save(query);
            }
        }

    }
}
