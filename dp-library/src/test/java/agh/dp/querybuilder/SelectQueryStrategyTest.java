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
    public void getTableNamesFromQueryTest1(){
        String query ="select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        List<String> names = selectQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("Student"));
    }

    @Test
    public void getTableNamesFromQueryTest2(){
        String query ="SELECT * FROM tabela1 tab1  left JOIN tabela2 tab2 right JOIN tabela3 tab3 join tabela4 tab4 WHERE condition";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        List<String> names = selectQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("tabela1"));
        Assert.assertThat(names.get(1), equalTo("tabela2"));
        Assert.assertThat(names.get(2), equalTo("tabela3"));
        Assert.assertThat(names.get(3), equalTo("tabela4"));
    }

    @Test
    public void getTableNamesFromQueryTest3(){
        String query ="SELECT * FROM tabela1 tab1  left JOIN tabela2 tab2 right JOIN tabela3 tab3 join tabela4 tab4 WHERE condition order by something";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        List<String> names = selectQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("tabela1"));
        Assert.assertThat(names.get(1), equalTo("tabela2"));
        Assert.assertThat(names.get(2), equalTo("tabela3"));
        Assert.assertThat(names.get(3), equalTo("tabela4"));
    }

    @Test
    public void getTableNamesFromQueryTest4(){
        String query ="SELECT * FROM tabela1 tab1  left JOIN tabela2 tab2 right JOIN tabela3 tab3 join tabela4 tab4 order by something";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        List<String> names = selectQueryStrategy.getTableNamesFromQuery(query);
        Assert.assertThat(names.get(0), equalTo("tabela1"));
        Assert.assertThat(names.get(1), equalTo("tabela2"));
        Assert.assertThat(names.get(2), equalTo("tabela3"));
        Assert.assertThat(names.get(3), equalTo("tabela4"));
    }
    @Test
    public void buildQueryTest1(){
        String query ="select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("select student0_.id as id1_0_0_, student0_.FIRSTNAME as FIRSTNAM2_0_0_, student0_.LASTNAME as LASTNAME3_0_0_ from Student student0_ where student0_.id=?"));
    }

    @Test
    public void buildQueryTest2(){
        String query ="SELECT * FROM Student";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM Student WHERE Student.id IN (1, 2)"));
    }

    @Test
    public void buildQueryTest3(){
        String query ="SELECT * FROM Student student where condition";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM Student student WHERE condition and student.id IN (1, 2)"));
    }

    @Test
    public void buildQueryTest4(){
        String query ="SELECT * FROM Student where condition";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM Student WHERE condition and student.id IN (1, 2)"));
    }

    @Test
    public void buildQueryTest5(){
        String query ="SELECT * FROM Student order by something";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("Student", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("Student",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM Student WHERE student.id IN (1, 2) order by something"));
    }


    @Test
    public void buildQueryTest6(){
        String query ="SELECT * FROM tabela1 tab1 left JOIN tabela2 tab2 right JOIN tabela3 tab3 join tabela4 tab4 WHERE condition order by something";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("tabela1", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("tabela1",PermissionsProvider.READ, (long)2,(long)1);
        Permission permission3 = new Permission("tabela2", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission4 = new Permission("tabela2",PermissionsProvider.READ, (long)2,(long)1);
        Permission permission5 = new Permission("tabela3", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission6 = new Permission("tabela3",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        permissions.add(permission3);
        permissions.add(permission4);
        permissions.add(permission5);
        permissions.add(permission6);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM tabela1 tab1 left JOIN tabela2 tab2 right JOIN tabela3 tab3 join tabela4 tab4 WHERE condition AND tab1.ID IN (1, 2) and tab2.ID IN (1, 2) and tab3.ID IN (1, 2) and tab4.ID == 0 order by something"));
    }

    @Test
    public void buildQueryTest7(){
        String query ="SELECT * FROM tabela1 tab1 left JOIN tabela2 tab2 right JOIN tabela3 join tabela4 tab4 WHERE condition order by something";
        SelectQueryStrategy selectQueryStrategy = new SelectQueryStrategy();
        Permission permission = new Permission("tabela1", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission2 = new Permission("tabela1",PermissionsProvider.READ, (long)2,(long)1);
        Permission permission3 = new Permission("tabela2", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission4 = new Permission("tabela2",PermissionsProvider.READ, (long)2,(long)1);
        Permission permission5 = new Permission("tabela3", PermissionsProvider.READ, (long)1,(long)1);
        Permission permission6 = new Permission("tabela3",PermissionsProvider.READ, (long)2,(long)1);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        permissions.add(permission2);
        permissions.add(permission3);
        permissions.add(permission4);
        permissions.add(permission5);
        permissions.add(permission6);
        String returnQuery = selectQueryStrategy.buildQuery(query, permissions);
        Assert.assertThat(returnQuery, containsStringIgnoringCase("SELECT * FROM tabela1 tab1 left JOIN tabela2 tab2 right JOIN tabela3 join tabela4 tab4 WHERE condition AND tab1.ID IN (1, 2) and tab2.ID IN (1, 2) and tabela3.ID IN (1, 2) and tab4.ID == 0 order by something"));
    }
}

