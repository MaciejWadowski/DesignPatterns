package agh.dp.querybuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {

    public List<String[]> getTableNames(String startingQuery){
        List<String[]> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("from", Pattern.CASE_INSENSITIVE);
        Pattern joinPattern = Pattern.compile("(left|right|outer)* ?join", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(where|order by)");
        Matcher matcher1 = fromPattern.matcher(builder);
        Matcher joinMatcher = joinPattern.matcher(builder);
        Matcher whereMatcher = endingOfTableNames.matcher(builder);

        int start, end, asStart, joinEnd;
        String name = "";
        String asName = "";
        String[] names2;

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
        end = joinMatcher.start();
        name = builder.substring(start,end);
        name = name.trim();
        names2 = name.split(" ");
        if (names2.length==2){
            names.add(names2);
        }
        else{
            names.set(2, names.get(1));
            names.add(names2);
        }
    }

    public String buildQuery(String startingQuery, Map<String,List<Long>> permissions){
        StringBuilder builder = new StringBuilder(startingQuery);
        // delete ":" from query
        builder.deleteCharAt(builder.length()-1);

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

    private void makeConstraints(Map<String, List<Long>> permissions, StringBuilder builder, List<String[]> tableNames) {
        List<Long> perms;
        for (int i = 0; i<tableNames.size(); i++) {
            perms = permissions.getOrDefault(tableNames.get(i)[0], null);
            if (perms == null){
                continue;
            }
            builder.append(tableNames.get(i)[1]+".ID IN (");
            for(long number: perms){
                builder.append(number+", ");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.deleteCharAt(builder.length()-1);
            builder.append(") ");
        }
    }

    public static void main(String[] args) {
        String s = "SELECT * FROM tab1 tabelka join tab2 babelka  join tab3 bombelek where JAJA order by jaja;";
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
