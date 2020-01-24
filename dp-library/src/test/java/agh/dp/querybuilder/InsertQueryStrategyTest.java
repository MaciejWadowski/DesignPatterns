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
    public void buildQueryTest1(){
        String query ="Insert into Student values (dane1, dane2, dane3)";
        InsertQueryStrategy insertQueryStrategy = new InsertQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.INSERT, 0L);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        String returnQuery = insertQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("Insert into Student values (dane1, dane2, dane3)"));
    }

    @Test
    public void buildQueryTest2(){
        String query ="INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)";
        InsertQueryStrategy insertQueryStrategy = new InsertQueryStrategy();
        Permission permission = new Permission("table_name", PermissionsProvider.INSERT, 0L);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        String returnQuery = insertQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)"));
    }


    @Test
    public void buildQueryTest3(){
        String query ="INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)";
        InsertQueryStrategy insertQueryStrategy = new InsertQueryStrategy();
        List<Permission> permissions = new ArrayList<>();
        String returnQuery = insertQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, equalTo(null));
    }

}
