package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.BlogCommentDAO;
import com.course.entity.BlogCommentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
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
            String hql = "FROM BlogCommentEntity b WHERE b.parentComment.id = :id";
            return session.createQuery(hql, BlogCommentEntity.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Long findNumberCommentOfBlog(Long blogId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b.id) FROM BlogCommentEntity b WHERE b.blog.id = :blogId";
            return session.createQuery(hql, Long.class)
                    .setParameter("blogId", blogId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<BlogCommentEntity> findListCommentByBlogSlug(String slug) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogCommentEntity b WHERE b.blog.slug = :slug ORDER BY b.createAt DESC";
            return session.createQuery(hql, BlogCommentEntity.class)
                    .setParameter("slug", slug)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    public List<BlogCommentEntity> findListNoParentCommentByBlogSlug(String slug) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Adjusted HQL to check for null in the parentComment field
            String hql = "FROM BlogCommentEntity b WHERE b.blog.slug = :slug AND b.parentComment IS NULL ORDER BY b.createAt DESC";
            return session.createQuery(hql, BlogCommentEntity.class)
                    .setParameter("slug", slug)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public void updateBlogComment(BlogCommentEntity blogComment) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(blogComment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update blog comment", e);
        }
    }

    @Override
    public void deleteBlogComment(BlogCommentEntity blogComment) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(blogComment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Xóa comment thất bại", e);
        }
    }
}
