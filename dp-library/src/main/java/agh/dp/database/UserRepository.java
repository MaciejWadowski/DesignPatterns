package agh.dp.database;

import agh.dp.models.Permission;
import agh.dp.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
