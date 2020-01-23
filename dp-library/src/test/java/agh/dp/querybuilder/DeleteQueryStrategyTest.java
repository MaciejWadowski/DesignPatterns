package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class DeleteQueryStrategyTest {
    @Test
    public void getTableNamesFromQueryTest(){
        String query ="Delete from Student where id=?";
        DeleteQueryStrategy deleteQueryStrategy = new DeleteQueryStrategy();
        List<String> names = deleteQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("Student"));
    }

    @Test
    public void buildQueryTest(){
        String query ="Delete * from Student";
        DeleteQueryStrategy deleteQueryStrategy = new DeleteQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.DELETE, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.DELETE, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = deleteQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("Delete * from Student WHERE Student.id IN (1, 2)"));
    }
}
