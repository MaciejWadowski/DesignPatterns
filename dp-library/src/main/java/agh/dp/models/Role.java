package agh.dp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String roleName;
    private String inheritedRoleName;

    public Role(String roleName, String inheritedRoleName){
        this.roleName = roleName;
        this.inheritedRoleName = inheritedRoleName;
    }

    public Role() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getInheritedRoleName() {
        return inheritedRoleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        if (roleName != null ? !roleName.equals(role.roleName) : role.roleName != null) return false;
        return inheritedRoleName != null ? inheritedRoleName.equals(role.inheritedRoleName) : role.inheritedRoleName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (inheritedRoleName != null ? inheritedRoleName.hashCode() : 0);
        return result;
    }

    public void setInheritedRoleName(String inheritedRoleName) {
        this.inheritedRoleName = inheritedRoleName;
    }

}
