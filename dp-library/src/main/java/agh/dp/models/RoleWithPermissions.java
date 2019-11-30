package agh.dp.models;

import agh.dp.providers.PermissionsProvider;

import java.util.*;

public class RoleWithPermissions {
    private String roleName;
    private String inheritedRoleName;
    private List<Permission> permissions;
    private long roleId;

    RoleWithPermissions(String roleName, String inheritedRoleName, List<Permission> permissions){
        this.roleName = roleName;
        this.inheritedRoleName = inheritedRoleName;
        this.permissions = permissions;
    }

    public String getRoleName(){
        return roleName;
    }

    public String getInheritedRoleName() {
        return inheritedRoleName;
    }

    public void assignUserToRole(String userName){
        User user = new User(userName, this.roleId);
        //TODO: save user to database
    }

    private RoleWithPermissions(RoleWithPermissionsBuilder roleWithPermissionsBuilder){
        this.roleName = roleWithPermissionsBuilder.roleName;
        this.inheritedRoleName = roleWithPermissionsBuilder.inheritedRoleName;
        this.permissions = roleWithPermissionsBuilder.permissions;
        this.roleId = roleWithPermissionsBuilder.roleId;
    }

    public static class RoleWithPermissionsBuilder {
        private String roleName;
        private String inheritedRoleName;
        private List<Permission>  permissions;
        private long roleId;

        public RoleWithPermissionsBuilder(String roleName){
            this.permissions = new ArrayList<Permission>();
            this.roleName = roleName;
        }

        public RoleWithPermissionsBuilder setInheritedRole(String inheritedRoleName){
            this.inheritedRoleName = inheritedRoleName;
            return this;
        }

        public RoleWithPermissionsBuilder addPermission(String tableName, int accessLevel, int recordId){
            Permission permission = new Permission(tableName, accessLevel, recordId);
            this.permissions.add(permission);
            return this;
        }

        public RoleWithPermissionsBuilder addPermissions(String tableName, int accessLevel, long ... recordIds){
            for (long id : recordIds){
                Permission permission = new Permission(tableName, accessLevel, id);
                this.permissions.add(permission);
            }
            return this;
        }

        public RoleWithPermissionsBuilder addInsertPermissions(String ... tableNames){
            for (String tableName : tableNames){
                Permission permission = new Permission(tableName, PermissionsProvider.INSERT, 0);
                this.permissions.add(permission);
            }
            return this;
        }

        public RoleWithPermissions build(){
            System.out.println("Role building" + this.roleName);
            Role role = new Role(this.roleName, this.inheritedRoleName);
            //TODO: save role to database,
            // get id of created record,
            // assign id to this.roleId,
            // assign to each permission
            for (Permission permission : permissions){
                permission.setRoleId(1);
                //TODO: save each permission to database
            }
            return new RoleWithPermissions(this);
        }
    }
}
