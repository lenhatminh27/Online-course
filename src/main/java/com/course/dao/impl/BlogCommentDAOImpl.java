package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.BlogCommentDAO;
import com.course.entity.BlogCommentEntity;
import com.course.entity.BlogEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BlogCommentDAOImpl implements BlogCommentDAO {
    @Override
    public BlogCommentEntity createBlogComment(BlogCommentEntity blogComment) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(blogComment);
            transaction.commit();
            return blogComment;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog comment", e);
        }
    }

    @Override
    public BlogCommentEntity findBlogCommentById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogCommentEntity b WHERE b.id = :id";
            return session.createQuery(hql, BlogCommentEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BlogCommentEntity> findAllChildrenBlogComments(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogCommentEntity b WHERE b.parentId = :id";
            return session.createQuery(hql, BlogCommentEntity.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
