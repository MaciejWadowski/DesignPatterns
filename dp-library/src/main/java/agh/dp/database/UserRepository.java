package agh.dp.database;

import agh.dp.models.Permission;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Permission, Long> {
}
