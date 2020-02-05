package agh.dp.querybuilder;

import agh.dp.models.Permission;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryStrategy implements  QueryStrategy{

    private List<String[]> getTableNames(String startingQuery){
        List<String[]> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("from", Pattern.CASE_INSENSITIVE);
        Pattern joinPattern = Pattern.compile("(left|right|outer)* ?join", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(where|order by)");
        Matcher matcher1 = fromPattern.matcher(builder);
        Matcher joinMatcher = joinPattern.matcher(builder);
        Matcher whereMatcher = endingOfTableNames.matcher(builder);

        int start, joinEnd;

        matcher1.find();
        start = matcher1.end()+1;
        joinEnd = start;

        while (true){
            if (joinMatcher.find(joinEnd)) {
                start = joinEnd;
                findNewName(names, builder, joinMatcher, start);
                joinEnd = joinMatcher.end()+1;
            }
            else {
                start = joinEnd;
                whereMatcher.find(joinEnd);
                findNewName(names, builder, whereMatcher, start);
                break;
            }
        }
        return names;
    }

    private void findNewName(List<String[]> names, StringBuilder builder, Matcher joinMatcher, int start) {
        int end;
        String name;
        String[] names2;
        end = builder.length();
        name = builder.substring(start, end);
        name = name.trim();
        names2 = name.split(" ");
        if (names2.length >+ 2) {
            Pattern invalidNames = Pattern.compile("(where|order|left|right|outer|join)", Pattern.CASE_INSENSITIVE);
            Matcher invalidNameMatcher = invalidNames.matcher(names2[1]);
            if (invalidNameMatcher.find()){
                names2[1] = names2[0];
            }
            names2 = Arrays.copyOfRange(names2, 0, 2);
        }
        if (names2 != null) {
            if (names2.length < 2) {
                    name = names2[0];
                    names2 = new String[2];
                    names2[1] = name;
                    names2[0] = name;
            }
            names.add(names2);
        }
    }

    public String buildQuery(String startingQuery, List<Permission> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);

        List<String[]> tableNames = getTableNames(startingQuery);

        Pattern orderByPattern = Pattern.compile("order by", Pattern.CASE_INSENSITIVE);
        Matcher orderByMatcher = orderByPattern.matcher(builder);
        Boolean flag = Boolean.FALSE;
        String order = "";

        if (orderByMatcher.find()) {
            flag = Boolean.TRUE;
            order = builder.substring(orderByMatcher.start(), builder.length());
            builder.delete(orderByMatcher.start(), builder.length());
        }

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

        if (flag){
            builder.append(order);
        }
        String result = builder.toString();
        result = result.replaceAll("( )+", " ");
        return result;
    }

    private void makeConstraints(Map<String, List<Long>> permissions, StringBuilder builder, List<String[]> tableNames) {
        List<Long> perms;
        for (String[] tableName : tableNames) {
            perms = permissions.getOrDefault(tableName[0], null);
            if (perms == null) {
                builder.append(tableName[1]).append(".ID = 0 AND ");
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
}
