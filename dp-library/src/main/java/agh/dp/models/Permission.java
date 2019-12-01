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

    public Permission() {}

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "tableName='" + tableName + '\'' +
                ", accessLevel=" + accessLevel +
                ", recordId=" + recordId +
                ", roleId=" + roleId +
                ", id=" + id +
                '}';
    }
}

