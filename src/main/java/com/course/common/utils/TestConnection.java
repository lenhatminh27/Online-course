package com.course.common.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConnection {
    private static final Logger logger = LoggerFactory.getLogger(TestConnection.class);

    public static void main(String[] args) {
        // Get the Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        // Open a session to test the connection
        Session session = null;
        try {
            session = sessionFactory.openSession();
            logger.info("Connection to the database was successful!");
        } catch (Exception e) {
            logger.error("Failed to connect to the database!", e);
        } finally {
            if (session != null) {
                session.close();
            }
            sessionFactory.close();
            logger.info("SessionFactory closed.");
        }
    }
}
