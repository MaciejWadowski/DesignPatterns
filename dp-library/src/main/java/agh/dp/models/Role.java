package agh.dp.models;

public class Role {
    private long id;
    private String roleName;
    private String inheritedRoleName;

    Role(String roleName, String inheritedRoleName){
        this.roleName = roleName;
        this.inheritedRoleName = inheritedRoleName;
    }
}
