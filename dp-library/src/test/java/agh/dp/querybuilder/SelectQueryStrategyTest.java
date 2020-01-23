package agh.dp.querybuilder;

import agh.dp.models.Permission;
import agh.dp.providers.PermissionsProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class SelectQueryStrategyTest {
    @Test
    public void getTableNamesFromQueryTest(){
        String query ="select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        List<String> names = selectQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("Student"));
    }

    @Test
    public void buildQueryTest(){
        String query ="select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.INSERT, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.INSERT, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?"));
    }
}
