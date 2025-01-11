package com.course.common.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;

import java.util.Set;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Load Hibernate configuration from hibernate.cfg.xml
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

            // Dynamically add entity classes using Reflections
            Reflections reflections = new Reflections("com.course.entity");  // package where entities are located
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

            // Add all found entity classes to the configuration
            for (Class<?> entityClass : annotatedClasses) {
                configuration.addAnnotatedClass(entityClass);
            }

            // Build a ServiceRegistry using the Configuration
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // Create the SessionFactory from the configuration and ServiceRegistry
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Log the exception and throw a runtime exception
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Return the SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Method to close the SessionFactory
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    // Method to get the current session
    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
