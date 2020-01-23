package agh.dp.querybuilder;

import agh.dp.models.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryStrategy extends QueryJoinedOperations implements QueryStrategy {

    private String getTableNameForInsert(String  startingQuery){
        String tableName;
        int start, end;
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern intoPattern = Pattern.compile("into", Pattern.CASE_INSENSITIVE);
        Matcher intoMatcher = intoPattern.matcher(builder);
        Pattern valuesPattern = Pattern.compile("values", Pattern.CASE_INSENSITIVE);
        Matcher valuesMatcher = valuesPattern.matcher(builder);

        intoMatcher.find();
        start = intoMatcher.end()+1;
        valuesMatcher.find();
        end = valuesMatcher.start();
        tableName = builder.substring(start,end);
        tableName = tableName.trim();
        tableName = tableName.split(" ")[0];

        return tableName;
    }

    public String buildQuery(String startingQuery, List<Permission> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);
        if (!permissions.isEmpty()){
            return builder.toString();
        }
        return null;
    }

    public List<String> getTableNamesFromQuery(String query){
        List<String> officialTableNames = new ArrayList<>();
        String tableName = getTableNameForInsert(query);
        officialTableNames.add(tableName);
        return officialTableNames;
    }
}
