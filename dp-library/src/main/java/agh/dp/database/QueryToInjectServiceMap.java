package agh.dp.database;

import agh.dp.models.Permission;
import agh.dp.models.QueryToInject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryToInjectServiceMap {
    private final QueryToInjectRepository repository;

    public QueryToInjectServiceMap(QueryToInjectRepository repository){
        this.repository = repository;
    }

    public QueryToInject getQueryToInjectByTableNameAndAccessLevel(String tableNames, int accessLevel){
        Set<QueryToInject> queries = new HashSet<>();
        repository.findAll().forEach(queries::add);
        return queries.stream()
                .filter(permission -> permission.getOperationLevel() == accessLevel && tableNames.equals(permission.getTableName())).findFirst().orElse(null);
    }
}
