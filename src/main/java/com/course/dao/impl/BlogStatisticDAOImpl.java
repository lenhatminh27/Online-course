package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.BlogStatisticDAO;
import com.course.entity.BlogStatisticEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BlogStatisticDAOImpl implements BlogStatisticDAO {

    @Override
    public BlogStatisticEntity createBlogStatistic(BlogStatisticEntity blog) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(blog);
            transaction.commit();
            return blog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public BlogStatisticEntity updateBlogStatistic(BlogStatisticEntity blogStatistic) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(blogStatistic);
            transaction.commit();
            return blogStatistic;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public BlogStatisticEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(BlogStatisticEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find blog by ID: " + id, e);
        }
    }
}
