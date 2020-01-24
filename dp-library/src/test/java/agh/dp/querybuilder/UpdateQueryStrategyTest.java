package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class UpdateQueryStrategyTest {
    @Test
    public void getTableNamesFromQueryTest(){
        String query ="update Student set FIRSTNAME=?, LASTNAME=? where id=?";
        UpdateQueryStrategy updateQueryStrategy = new UpdateQueryStrategy();
        List<String> names = updateQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("Student"));
    }

    @Test
    public void buildQueryTest(){
        String query ="update Student set FIRSTNAME=?, LASTNAME=? where id=?";
        UpdateQueryStrategy updateQueryStrategy = new UpdateQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.UPDATE, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.UPDATE, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = updateQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("update Student set FIRSTNAME=?, LASTNAME=? where id=? and Student.id IN (1, 2)"));
    }

    @Test
    public void buildQueryTest2(){
        String query ="update Student set FIRSTNAME=?, LASTNAME=? where id=?";
        UpdateQueryStrategy updateQueryStrategy = new UpdateQueryStrategy();
        List<Permission> permissions = new ArrayList<>();
        String returnQuery = updateQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("update Student set FIRSTNAME=?, LASTNAME=? where id=? and Student.id == 0"));
    }
}
