package agh.dp.querybuilder;

import agh.dp.models.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class QueryJoinedOperations implements QueryStrategy{
    static List<String> getStrings(List<String> names, StringBuilder builder, Pattern fromPattern, Pattern endingOfTableNames) {
        Matcher matcher1 = fromPattern.matcher(builder);
        Matcher whereMatcher = endingOfTableNames.matcher(builder);
        int start, end;
        String name;
        matcher1.find();
        start = matcher1.end()+1;
        if(whereMatcher.find()){
            end = whereMatcher.start();
        }
        else {
            end = builder.length();
        }
        name = builder.substring(start,end);
        name = name.trim();
        String[] splitted = name.split(" ");
        names.add(splitted[0]);
        return names;
    }

    protected void makeConstraints(Map<String, List<Long>> permissions, StringBuilder builder, List<String> tableNames) {
        List<Long> perms;
        for (String tableName : tableNames) {
            perms = permissions.getOrDefault(tableName, null);
            if (perms == null) {
                continue;
            }
            builder.append(tableName).append(".ID IN (");
            for (long number : perms) {
                builder.append(number).append(", ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
            builder.append(") AND ");
        }
        builder.delete(builder.length()-4, builder.length());
    }

    public abstract  List<String> getTableNamesFromQuery(String startingQuery);

    public String buildQuery(String startingQuery, List<Permission> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);
        List<String> tableNames = getTableNamesFromQuery(startingQuery);
        Pattern wherePattern = Pattern.compile("where", Pattern.CASE_INSENSITIVE);
        Matcher whereMatcher = wherePattern.matcher(builder);

        if (!whereMatcher.find()){
            builder.append(" WHERE ");
        }
        else {
            builder.append(" AND ");
        }

        Map<String, List<Long>> dataForConstraints = new HashMap<>();
        for (Permission permission : permissions){
            if (!dataForConstraints.containsKey(permission.getTableName())){
                dataForConstraints.put(permission.getTableName(), new ArrayList<Long>());
            }
            List<Long> currentValue = dataForConstraints.get(permission.getTableName());
            currentValue.add(permission.getRecordId());
            dataForConstraints.replace(permission.getTableName(), currentValue);
        }
        makeConstraints(dataForConstraints, builder, tableNames);
        return builder.toString();
    }
}
