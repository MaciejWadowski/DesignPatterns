package agh.dp.database;

import agh.dp.models.Role;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.MapKeyColumn;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
