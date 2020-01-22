package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import agh.dp.querybuilder.QueryBuilder;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

public class Logging extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;
    // Define a static logger

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

        QueryBuilder queryBuilder = new QueryBuilder();
        int accessNeededForOperation = queryBuilder.getAccessLevelOfOperation(sql);
        if (accessNeededForOperation == PermissionsProvider.INSERT){

        } else {
            List<String> tableNames = queryBuilder.getTableNamesFromQuery(sql);
            List<Permission> permissions = Executor.getUserPermissions(getCurrentUsername(),
                    tableNames,
                    accessNeededForOperation);

            restrictedSql = queryBuilder.buildQuery(sql, permissions);
        }
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