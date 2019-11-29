package agh.dp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permission {
    private String tableName;
    private int accessLevel;
    private long recordId;
    private long roleId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Permission(String tableName, int accessLevel, long recordId){
        this.accessLevel = accessLevel;
        this.recordId = recordId;
        this.tableName = tableName;
    }

    public Permission(String tableName, int accessLevel, long recordId, long roleId, long id){
        this.tableName = tableName;
        this.accessLevel = accessLevel;
        this.recordId = recordId;
        this.roleId = roleId;
    }

    public void setRoleId(long roleId){
        this.roleId = roleId;
    }
}

