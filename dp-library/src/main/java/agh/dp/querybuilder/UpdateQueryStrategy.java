package agh.dp.querybuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UpdateQueryStrategy extends QueryJoinedOperations implements QueryStrategy {

    public List<String> getTableNamesFromQuery(String startingQuery){
        List<String> names = new ArrayList<>();
        StringBuilder builder = new StringBuilder(startingQuery);
        Pattern fromPattern = Pattern.compile("(update)", Pattern.CASE_INSENSITIVE);
        Pattern endingOfTableNames = Pattern.compile("(set)", Pattern.CASE_INSENSITIVE);
        return getStrings(names, builder, fromPattern, endingOfTableNames);
    }
}
