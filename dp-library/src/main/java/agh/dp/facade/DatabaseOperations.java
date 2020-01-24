package agh.dp.facade;

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
            org.hibernate.Transaction tr = session.beginTransaction();
            Object o = session.get(clazz, key);
            if (o == null) {
                o = object;
            }
            session.delete(o);
            tr.commit();
        } catch (Exception e) {
            flag = false;
        }
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

    public Object load(Class clazz, Serializable key) {
        Object obj;
        try {
            obj = session.load(clazz, key);
            String var = obj.toString();
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public Object get(Class clazz, Serializable key) {
        return session.get(clazz, key);
    }

}
