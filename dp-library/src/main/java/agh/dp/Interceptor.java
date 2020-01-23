package agh.dp;

import agh.dp.Workers.Executor;
import agh.dp.exceptions.UserPermissionException;
import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import agh.dp.querybuilder.*;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Interceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;
    private boolean rollbackFlag = false;
    private Executor executor;

    public Interceptor(Executor executor) {
        this.executor = executor;
    }

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
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        //executor.hasUserPermissionToRecord(getCurrentUsername(), )
        String objectType = entity.getClass().toString().substring(entity.getClass().toString().lastIndexOf(".")+1, entity.getClass().toString().length());
        if (!executor.hasUserPermissionToRecord(getCurrentUsername(), objectType, PermissionsProvider.DELETE, (Long)id)){
            rollbackFlag = true;
        }
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public String onPrepareStatement(String sql) {
        System.out.println(sql);
        String restrictedSql = sql;
        List<String> tableNames = new ArrayList<>();

        QueryStrategy queryStrategy = null;
        int accessNeededForOperation = getAccessLevelOfOperation(sql);
        if (accessNeededForOperation == PermissionsProvider.INSERT){
            queryStrategy = new InsertQueryStrategy();
        }
//        else if (accessNeededForOperation == PermissionsProvider.DELETE){
//            queryStrategy = new DeleteQueryStrategy();
//        }
        else if (accessNeededForOperation == PermissionsProvider.UPDATE) {
            queryStrategy = new UpdateQueryStrategy();
        }
        else if (accessNeededForOperation == PermissionsProvider.READ) {
            queryStrategy = new SelectQueryStrategy();
        }
        if (queryStrategy != null) {
            tableNames = queryStrategy.getTableNamesFromQuery(sql);
            List<Permission> permissions = executor.getUserPermissions(getCurrentUsername(),
                    tableNames,
                    accessNeededForOperation);

            restrictedSql = queryStrategy.buildQuery(sql, permissions);
        }
        if (restrictedSql == null){
            rollbackFlag = true;
            return super.onPrepareStatement(sql);
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

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        if (rollbackFlag) {
            rollbackFlag = false;
            tx.rollback();
        }
    }


}