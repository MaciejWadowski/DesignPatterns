package agh.dp.database;

import agh.dp.models.Permission;
import agh.dp.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionServiceMap implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceMap(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> findAllByRoleIds(List<Long> roleIds) {
        Set<Permission> permissions = new HashSet<>();
        permissionRepository.findAll().forEach(permissions::add);
        return permissions.stream()
                .filter(permission -> roleIds.contains(permission.getRoleId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findAllByRoleIdsAndTableName(List<Long> roleIds, String tableName) {
        Set<Permission> permissions = new HashSet<>();
        permissionRepository.findAll().forEach(permissions::add);
        return permissions.stream()
                .filter(permission -> roleIds.contains(permission.getRoleId()) && permission.getTableName().equals(tableName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findAllByRoleIdsAndTableNameAndAccessLevel(List<Long> roleIds, String tableName, int accessLevel) {
        Set<Permission> permissions = new HashSet<>();
        permissionRepository.findAll().forEach(permissions::add);
        return permissions.stream()
                .filter(permission -> roleIds.contains(permission.getRoleId()) && permission.getTableName().equals(tableName) && permission.getAccessLevel().equals(accessLevel))
                .collect(Collectors.toList());
    }
}
