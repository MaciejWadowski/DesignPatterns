package agh.dp;

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

    private RoleWithPermissions(RoleBuilder roleBuilder){
        this.roleName = roleBuilder.roleName;
        this.inheritedRoleName = roleBuilder.inheritedRoleName;
        this.permissions = roleBuilder.permissions;
        this.roleId = roleBuilder.roleId;
    }

    public static class RoleBuilder{
        private String roleName;
        private String inheritedRoleName;
        private List<Permission>  permissions;
        private long roleId;

        public RoleBuilder(String roleName){
            this.permissions = new ArrayList<Permission>();
            this.roleName = roleName;
        }

        public RoleBuilder setInheritedRole(String inheritedRoleName){
            this.inheritedRoleName = inheritedRoleName;
            return this;
        }

        public RoleBuilder addPermission(String tableName, int accessLevel, int recordId){
            Permission permission = new Permission(tableName, accessLevel, recordId);
            this.permissions.add(permission);
            return this;
        }

        public RoleBuilder addPermissions(String tableName, int accessLevel, long ... ids){
            for (long id : ids){
                Permission permission = new Permission(tableName, accessLevel, id);
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
