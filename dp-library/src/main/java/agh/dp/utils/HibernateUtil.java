package agh.dp.utils;

import java.util.HashMap;
import java.util.Map;

import agh.dp.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

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
}