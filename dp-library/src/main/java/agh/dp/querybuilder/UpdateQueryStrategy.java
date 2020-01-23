package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class UpdateQueryStrategy extends QueryJoinedOperations implements QueryStrategy {

    protected List<String> getTableNames(String startingQuery){
        List<String> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("(update)", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(set)", Pattern.CASE_INSENSITIVE);
        return getStrings(names, builder, fromPattern, endingOfTableNames);
    }

    public List<String> getTableNamesFromQuery(String query){
        List<String> tableNames = getTableNames(query);
        return tableNames;
    }


    public static void main(String[] args) {
        String s = "update Student set FIRSTNAME=?, LASTNAME=? where id=?";
        UpdateQueryStrategy queryBuilder = new UpdateQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.UPDATE, (long)1, (long)1);
        String s2 = queryBuilder.buildQuery(s, Collections.singletonList(permission));
        System.out.println(s2);
    }
}
