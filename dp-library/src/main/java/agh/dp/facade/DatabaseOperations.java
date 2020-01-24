package agh.dp.facade;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.transaction.TransactionScoped;
import java.io.Serializable;

public class DatabaseOperations {

    private final Session session;


    public DatabaseOperations(Session session) {
        this.session = session;
    }

    public Long save(Object object) {
        try{
        org.hibernate.Transaction tr = session.beginTransaction();
        Long longs = (Long) session.save(object);
        session.evict(object);
        TransactionStatus status = tr.getStatus();
        tr.commit();
        TransactionStatus status1 = tr.getStatus();
        } catch (RuntimeException e){
            return 0L;
        }
        return 1L;
    }

    public Boolean delete(Object object, Class clazz, Serializable key) {
        Boolean flag = true;
        try {
            org.hibernate.Transaction tr = session.beginTransaction();
            Object o = session.get(clazz, key);
            if (o == null) {
                tr.rollback();
                return false;
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
            obj.toString();
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public Object get(Class clazz, Serializable key) {
        return session.get(clazz, key);
    }

}
