package agh.dp.database;

import agh.dp.models.QueryToInject;
import org.springframework.data.repository.CrudRepository;

public interface QueryToInjectRepository extends CrudRepository<QueryToInject, Long> {
}
