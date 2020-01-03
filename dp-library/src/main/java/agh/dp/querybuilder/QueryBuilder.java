package agh.dp.querybuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {

    public List<String> getTableNames(String startingQuery){
        List<String> names = new ArrayList<String>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("from", Pattern.CASE_INSENSITIVE);
        Pattern joinPattern = Pattern.compile("join", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableName = Pattern.compile(" ");
        Matcher matcher1 = fromPattern.matcher(builder);
        int start, end;

        matcher1.find();
        start = matcher1.start()+5;

        matcher1 = endingOfTableName.matcher(builder);
        matcher1.find(start);
        end = matcher1.start();
        names.add(builder.substring(start,end));
        while (true){
            matcher1 = joinPattern.matcher(builder);
            if (matcher1.find()) {
                start = matcher1.start()+5;
                matcher1 = endingOfTableName.matcher(builder);
                matcher1.find(start);
                end = matcher1.start();
                names.add(builder.substring(start, end));
                builder.delete(0,end);

            }
            else {
                break;
            }
        }
        return names;
    }

    public String buildQuery(String startingQuery, Map<String,List<Long>> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);
        // delete ":" from query
        builder.deleteCharAt(builder.length()-1);

        List<String> tableNames = getTableNames(startingQuery);

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
            makeConstraints(permissions, builder, tableNames);
        }
        else {
            builder.append(" AND ");
            makeConstraints(permissions, builder, tableNames);
        }

        if (flag){
            builder.append(order);
        }
        builder.append(":");
        return builder.toString();
    }

    private void makeConstraints(Map<String, List<Long>> permissions, StringBuilder builder, List<String> tableNames) {
        List<Long> perms;
        for (int i = 0; i<tableNames.size(); i++) {
            perms = permissions.getOrDefault(tableNames.get(i), null);
            if (perms == null){
                continue;
            }
            builder.append(tableNames.get(i)+".ID IN (");
            for(long number: perms){
                builder.append(number+", ");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.deleteCharAt(builder.length()-1);
            builder.append(") ");
        }
    }

    public static void main(String[] args) {
        String s = "SELECT * FROM tab1 join tab2 join tab3 where JAJA order by jaja;";
        QueryBuilder queryBuilder = new QueryBuilder();
        List<Long> l = Arrays.asList((long)1, (long)2);
        Map<String, List<Long>> map = new HashMap();
        map.put("tab1", l);
        map.put("tab2", l);
        map.put("tab3", l);

        String s2 = queryBuilder.buildQuery(s, map);
        System.out.println(s2);
    }

}
