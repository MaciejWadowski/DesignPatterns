package agh.dp.database;

import agh.dp.models.Permission;
import agh.dp.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionServiceMap{
    private final PermissionRepository permissionRepository;

    public PermissionServiceMap(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAllByRoleIdsAndTableNameAndAccessLevel(List<Long> roleIds, String tableName, int accessLevel) {
        Set<Permission> permissions = new HashSet<>();
        permissionRepository.findAll().forEach(permissions::add);
        return permissions.stream()
                .filter(permission -> roleIds.contains(permission.getRoleId()) && permission.getTableName().equals(tableName) && permission.getAccessLevel().equals(accessLevel))
                .collect(Collectors.toList());
    }
}
