package agh.dp.models;

import javax.persistence.*;

@Entity
public class Role {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(name ="ROLENAME")
    private String roleName;
    @Column(name= "INHERITEDROLEID")
    private Long inheritedRoleId;

    public Role() {}

    public Role(String roleName, Long inheritedRoleId) {
        this.roleName = roleName;
        this.inheritedRoleId = inheritedRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (roleName != null ? !roleName.equals(role.roleName) : role.roleName != null) return false;
        return inheritedRoleId != null ? inheritedRoleId.equals(role.inheritedRoleId) : role.inheritedRoleId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (inheritedRoleId != null ? inheritedRoleId.hashCode() : 0);
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

    public Long getInheritedRoleId() {
        return inheritedRoleId;
    }

    public void setInheritedRoleId(Long inheritedRoleName) {
        this.inheritedRoleId = inheritedRoleName;
    }
}
