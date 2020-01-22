package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import agh.dp.querybuilder.*;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;

public class Logging extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;
    // Define a static logger
    private int getAccessLevelOfOperation(String query){
        if (query.toLowerCase().contains("insert")){
            return PermissionsProvider.INSERT;
        } else if (query.toLowerCase().contains("select")){
            return PermissionsProvider.READ;
        } else if (query.toLowerCase().contains("delete")){
            return PermissionsProvider.DELETE;
        } else if (query.toLowerCase().contains("update")){
            return PermissionsProvider.UPDATE;
        }
        return 0;
    }

    @Override
    public boolean onSave(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {
        System.out.println("");
        return false;
    }
    // Logging SQL statement
    @Override
    public String onPrepareStatement(String sql) {
        System.out.println(sql);
        String restrictedSql = sql;

        QueryStrategy queryStrategy;
        int accessNeededForOperation = getAccessLevelOfOperation(sql);
        if (accessNeededForOperation == PermissionsProvider.INSERT){
            queryStrategy = new InsertQueryStrategy();

        }
        else if (accessNeededForOperation == PermissionsProvider.DELETE){
            queryStrategy = new DeleteQueryStrategy();
        }
        else if (accessNeededForOperation == PermissionsProvider.UPDATE) {
            queryStrategy = new UpdateQueryStrategy();
        }
        else {
            queryStrategy = new SelectQueryStrategy();
        }
        List<String> tableNames = queryStrategy.getTableNamesFromQuery(sql);
        List<Permission> permissions = Executor.getUserPermissions(getCurrentUsername(),
                tableNames,
                accessNeededForOperation);

        restrictedSql = queryStrategy.buildQuery(sql, permissions);
        return super.onPrepareStatement(restrictedSql);
    }

    private String getCurrentUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}