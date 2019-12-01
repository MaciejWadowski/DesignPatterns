package agh.dp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String roleName;
    private String inheritedRoleName;

    public Role() {}

    public Role(String roleName, String inheritedRoleName) {
        this.roleName = roleName;
        this.inheritedRoleName = inheritedRoleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (roleName != null ? !roleName.equals(role.roleName) : role.roleName != null) return false;
        return inheritedRoleName != null ? inheritedRoleName.equals(role.inheritedRoleName) : role.inheritedRoleName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (inheritedRoleName != null ? inheritedRoleName.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setInheritedRoleName(String inheritedRoleName) {
        this.inheritedRoleName = inheritedRoleName;
    }
}
