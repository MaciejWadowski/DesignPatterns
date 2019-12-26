package agh.dp.database;

import org.springframework.data.repository.CrudRepository;
import agh.dp.models.*;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
}