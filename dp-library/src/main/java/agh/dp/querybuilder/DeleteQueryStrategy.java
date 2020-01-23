package agh.dp.querybuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DeleteQueryStrategy extends QueryJoinedOperations implements QueryStrategy {

    public List<String> getTableNamesFromQuery(String startingQuery){
        List<String> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("(from)", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(where|;)", Pattern.CASE_INSENSITIVE);
        return getStrings(names, builder, fromPattern, endingOfTableNames);
    }
}
