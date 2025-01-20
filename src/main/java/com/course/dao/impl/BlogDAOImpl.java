package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.BlogDAO;
import com.course.entity.BlogEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BlogDAOImpl implements BlogDAO {

    @Override
    public BlogEntity createBlog(BlogEntity blog) {
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
    public boolean existsSlug(String slug) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM BlogEntity b WHERE b.slug = :slug";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("slug", slug)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check slug existence", e);
        }
    }

    @Override
    public boolean existTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT 1 FROM BlogEntity b WHERE b.title = :title";
            Object result = session.createQuery(hql)
                    .setParameter("title", title)
                    .uniqueResult();
            return result != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find blog by title", e);
        }
    }

}
