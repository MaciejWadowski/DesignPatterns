package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQueryStrategy implements QueryStrategy {

    private List<String[]> getTableNames(String startingQuery){
        List<String[]> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("(update)", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(set)", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = fromPattern.matcher(builder);
        Matcher whereMatcher = endingOfTableNames.matcher(builder);
        int start;
        matcher1.find();
        start = matcher1.end()+1;
        whereMatcher.find();
        findNewName(names, builder, whereMatcher, start);
        return names;
    }

    private void findNewName(List<String[]> names, StringBuilder builder, Matcher matcher, int start) {
        int end;
        String name;
        String[] names2;
        if(matcher.find()){
            end = matcher.start();
            name = builder.substring(start,end);
            name = name.trim();
            names2 = name.split(" ");
            if (names2.length != 2) {
                names.set(1, names.get(0));
            }
            names.add(names2);
        }
    }

    public String buildQuery(String startingQuery, List<Permission> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);

        List<String[]> tableNames = getTableNames(startingQuery);

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

    private void makeConstraints(Map<String, List<Long>> permissions, StringBuilder builder, List<String[]> tableNames) {
        List<Long> perms;
        for (String[] tableName : tableNames) {
            perms = permissions.getOrDefault(tableName[0], null);
            if (perms == null) {
                continue;
            }
            builder.append(tableName[1]).append(".ID IN (");
            for (long number : perms) {
                builder.append(number).append(", ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
            builder.append(") AND ");
        }
        builder.delete(builder.length()-4, builder.length());
    }

    public List<String> getTableNamesFromQuery(String query){
        List<String[]> tableNamePairs = getTableNames(query);
        List<String> officialTableNames = new ArrayList<>();
        for (String[] tableNamePair : tableNamePairs){
            officialTableNames.add(tableNamePair[0]);
        }
        return officialTableNames;
    }


//    public static void main(String[] args) {
//        String s = "UPDATE table_name tab set column1 = value1, column2 = value2, ... WHERE condition;";
//        UpdateQueryStrategy queryBuilder = new UpdateQueryStrategy();
//        Permission permission = new Permission("table_name", PermissionsProvider.UPDATE, (long)1, (long)1);
//        String s2 = queryBuilder.buildQuery(s, Collections.singletonList(permission));
//        System.out.println(s2);
//    }
}
