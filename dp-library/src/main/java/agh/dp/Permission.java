package agh.dp;

class Permission {
    private String tableName;
    private int accessLevel;
    private long recordId;
    private long roleId;
    private long id;

    public Permission(String tableName, int accessLevel, long recordId){
        this.accessLevel = accessLevel;
        this.recordId = recordId;
        this.tableName = tableName;
    }

    public void setRoleId(long roleId){
        this.roleId = roleId;
    }
}

