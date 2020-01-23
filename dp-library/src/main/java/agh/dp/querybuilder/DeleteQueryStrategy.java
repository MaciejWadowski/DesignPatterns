package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;

import java.util.ArrayList;
import java.util.Collections;
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


    public static void main(String[] args) {
        String s = "delete from Student where id=?";
        DeleteQueryStrategy queryBuilder = new DeleteQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.DELETE, (long)1, (long)1);
        String s2 = queryBuilder.buildQuery(s, Collections.singletonList(permission));
        System.out.println(s2);
    }

}
