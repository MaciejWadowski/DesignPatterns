package agh.dp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permission {

    private String tableName;
    private Integer accessLevel;
    private Long recordId;
    private Long roleId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Permission() {}

    public Permission(String tableName, Integer accessLevel, Long recordId, Long roleId) {
        this.tableName = tableName;
        this.accessLevel = accessLevel;
        this.recordId = recordId;
        this.roleId = roleId;
    }

    public Permission(String tableName, Integer accessLevel) {
        this.tableName = tableName;
        this.accessLevel = accessLevel;
    }

    public Permission(String tableName, Integer accessLevel, Long recordId) {
        this.tableName = tableName;
        this.accessLevel = accessLevel;
        this.recordId = recordId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (accessLevel != null ? !accessLevel.equals(that.accessLevel) : that.accessLevel != null) return false;
        if (recordId != null ? !recordId.equals(that.recordId) : that.recordId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (accessLevel != null ? accessLevel.hashCode() : 0);
        result = 31 * result + (recordId != null ? recordId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}