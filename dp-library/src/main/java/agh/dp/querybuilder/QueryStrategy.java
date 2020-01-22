package agh.dp.querybuilder;

import agh.dp.models.Permission;

import java.util.List;

public interface QueryStrategy {
    public String buildQuery(String startingQuery, List<Permission> permissions);
    public List<String> getTableNamesFromQuery(String query);
}
