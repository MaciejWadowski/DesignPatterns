package agh.dp.models;

public class User {
    private String userName;
    private long roleId;
    private long id;

    public User(String userName, long roleId){
        this.userName = userName;
        this.roleId = roleId;
    }
}
