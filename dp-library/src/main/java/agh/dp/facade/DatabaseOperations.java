package agh.dp.facade;

import agh.dp.exceptions.UserPermissionException;
import org.hibernate.Session;

import java.io.Serializable;

public class DatabaseOperations {

    private final Session session;

    public DatabaseOperations(Session session) {
        this.session = session;
    }

    public Long save(Object object) {
        org.hibernate.Transaction tr = session.beginTransaction();
        Long longs = (Long) session.save(object);
        session.evict(object);
        tr.commit();
        return longs;
    }

    public Boolean delete(Object object, Class clazz, Serializable key) {
        Boolean flag = true;
        try {
            Object o = session.load(clazz, key);
            org.hibernate.Transaction tr = session.beginTransaction();
            if (o == null) {
                o = object;
            }
            session.delete(o);
            tr.commit();
        } catch (UserPermissionException e) {
            flag = false;
            System.out.println("LOL1");
        }
        System.out.println("LOL");
        return flag;
    }

    public Boolean update(Object object) {
        Boolean flag = true;
        try {
            org.hibernate.Transaction tr = session.beginTransaction();
            session.update(object);
            tr.commit();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }



    public Object get(Class clazz, Serializable key) {
        return session.get(clazz, key);
    }

}
