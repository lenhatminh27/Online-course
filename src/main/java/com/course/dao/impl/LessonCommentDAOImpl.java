package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.LessonCommentDAO;
import com.course.entity.BlogCommentEntity;
import com.course.entity.LessonCommentEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
public class LessonCommentDAOImpl implements LessonCommentDAO {

    @Override
    public LessonCommentEntity createLessonComment(LessonCommentEntity lessonComment) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(lessonComment);
            transaction.commit();
            return lessonComment;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create lesson comment", e);
        }
    }

    @Override
    public List<LessonCommentEntity> findAllChildrenLessonComments(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM LessonCommentEntity b WHERE b.parentLessonComment.id = :id";
            List<LessonCommentEntity> lessonComments = session.createQuery(hql, LessonCommentEntity.class)
                    .setParameter("id", id)
                    .getResultList();
            lessonComments.forEach(lessonCommentEntity -> {
                Hibernate.initialize(lessonCommentEntity.getCourseLesson());
                Hibernate.initialize(lessonCommentEntity.getAccount());
            });
            return lessonComments;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public LessonCommentEntity findLessonCommentById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM LessonCommentEntity b WHERE b.id = :id";
            return session.createQuery(hql, LessonCommentEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteLessonCommentByLessonId(Long lessonId) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String hql = "DELETE FROM LessonCommentEntity lc WHERE lc.courseLesson.id = :id";
            int deletedCount = session.createQuery(hql)
                    .setParameter("id", lessonId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to delete lesson comment", e);
        }
    }

    @Override
    public List<LessonCommentEntity> findNoParentLessonCommentsByLessonId(Long lessonId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM LessonCommentEntity b WHERE b.courseLesson.id = :lessonId AND b.parentLessonComment.id IS NULL";
            List<LessonCommentEntity> comments = session.createQuery(hql, LessonCommentEntity.class)
                    .setParameter("lessonId", lessonId)
                    .getResultList();
            comments.forEach(commentEntity -> {
                Hibernate.initialize(commentEntity.getCourseLesson());
                Hibernate.initialize(commentEntity.getAccount());
            });
            return comments;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
