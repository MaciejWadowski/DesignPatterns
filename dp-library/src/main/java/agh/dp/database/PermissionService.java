package agh.dp.database;

import agh.dp.models.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAllByRoleIds(List<Long> roleIds);
    List<Permission> findAllByRoleIdsAndTableName(List<Long> roleIds, String tableName);
    List<Permission> findAllByRoleIdsAndTableNameAndAccessLevel(List<Long> roleIds, String tableName, int accessLevel);
}
