package agh.dp.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agh.dp.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(String driver, String url, String user, String password, Class ... classes) {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder
                        = new StandardServiceRegistryBuilder();

                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DRIVER, driver);
                settings.put(Environment.URL, url);
                settings.put(Environment.USER, user);
                settings.put(Environment.PASS, password);
                settings.put(Environment.HBM2DDL_AUTO, "update");
                settings.put( Environment.USE_QUERY_CACHE, Boolean.FALSE.toString() );
                settings.put( Environment.USE_SECOND_LEVEL_CACHE, Boolean.FALSE.toString() );
                //settings.put(Environment.CACHE_REGION_FACTORY,org.hibernate.cache.impl.NoCachingRegionFactory.class.getName());
                //settings.put(Environment.CACHE_PROVIDER,org.hibernate.cache.NoCacheProvider.class.getName());

                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);

                for (Class clazz: classes) {
                    sources.addAnnotatedClass(clazz);
                }

                Object obj = sources.getMetadataBuilder();
                Metadata metadata = sources.getMetadataBuilder().build();

                //  To apply logging Interceptor using session factory
                sessionFactory = metadata.getSessionFactoryBuilder()
//                .applyInterceptor(new LoggingInterceptor())
                        .build();
            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }
}