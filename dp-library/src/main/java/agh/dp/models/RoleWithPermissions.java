package agh.dp.models;

import agh.dp.providers.PermissionsProvider;

import java.util.*;

public class RoleWithPermissions {
    private Role role;
    private List<Permission> permissions;

    public RoleWithPermissions() {}

    public RoleWithPermissions(String roleName, Long inheritedRoleName, List<Permission> permissions){
        this.role = new Role();
        this.permissions = new ArrayList<>();
        this.role.setRoleName(roleName);
        this.role.setInheritedRoleId(inheritedRoleName);
        this.permissions = permissions;
    }

    public String getRoleName(){
        return role.getRoleName();
    }

    public Long getInheritedRoleName() {
        return role.getInheritedRoleId();
    }

    public void assignUserToRole(String userName){
        User user = new User(userName, this.role.getId());
        //TODO: save user to database
    }

    public List<Permission> getPermissions(){
        return permissions;
    }

    public Role getRole(){
        return role;
    }

    private RoleWithPermissions(RoleWithPermissionsBuilder roleWithPermissionsBuilder){
        this.role = new Role();
        this.permissions = new ArrayList<>();
        this.role.setRoleName(roleWithPermissionsBuilder.role.getRoleName());
        this.role.setInheritedRoleId(roleWithPermissionsBuilder.role.getInheritedRoleId());
        this.permissions = roleWithPermissionsBuilder.permissions;
    }

    public static class RoleWithPermissionsBuilder {
        private Role role;
        private List<Permission> permissions;

        public RoleWithPermissionsBuilder(String roleName){
            this.permissions = new ArrayList<Permission>();
            this.role = new Role();
            this.role.setRoleName(roleName);
        }

        public RoleWithPermissionsBuilder setInheritedRole(Long inheritedRoleName){
            this.role.setInheritedRoleId(inheritedRoleName);
            return this;
        }

        public RoleWithPermissionsBuilder addPermission(String tableName, Integer accessLevel, Integer recordId){
            Permission permission = new Permission(tableName, accessLevel, Long.valueOf(recordId));
            this.permissions.add(permission);
            return this;
        }

        public RoleWithPermissionsBuilder addPermissions(String tableName, Integer accessLevel, Integer ... recordIds){
            for (Integer id : recordIds){
                Long longId = (long) id;
                Permission permission = new Permission(tableName, accessLevel, longId);
                this.permissions.add(permission);
            }
            return this;
        }

        public RoleWithPermissionsBuilder addInsertPermissions(String ... tableNames){
            for (String tableName : tableNames){
                Permission permission = new Permission(tableName, PermissionsProvider.INSERT, 0L);
                this.permissions.add(permission);
            }
            return this;
        }

        public RoleWithPermissions build(){
            return new RoleWithPermissions(this);
        }
    }
}
