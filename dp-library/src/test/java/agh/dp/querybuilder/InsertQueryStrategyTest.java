package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class InsertQueryStrategyTest {
    @Test
    public void getTableNamesFromQueryTest(){
        String query ="Insert into Student values (dane1, dane2, dane3)";
        InsertQueryStrategy insertQueryStrategy = new InsertQueryStrategy();
        List<String> names = insertQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("Student"));
    }

    @Test
    public void buildQueryTest(){
        String query ="Insert into Student values (dane1, dane2, dane3)";
        InsertQueryStrategy insertQueryStrategy = new InsertQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.INSERT, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.INSERT, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = insertQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("Insert into Student values (dane1, dane2, dane3)"));
    }
}
