package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.LessonDAO;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
public class LessonDAOImpl implements LessonDAO {

    @Override
    public CourseLessonEntity createLesson(CourseLessonEntity lesson) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(lesson);
            transaction.commit();
            return lesson;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public CourseLessonEntity updateLesson(CourseLessonEntity lesson) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(lesson);
            transaction.commit();
            return lesson;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public boolean existsByTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(c) FROM CourseLessonEntity c WHERE c.title = :title";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("title", title)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check title existence", e);
        }
    }

    public CourseLessonEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CourseLessonEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countExistSection(CourseSectionEntity section) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(cl) FROM CourseLessonEntity cl WHERE cl.courseSection.id = :sectionId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("sectionId", section.getId())
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<CourseLessonEntity> findBySection(CourseSectionEntity section) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT cl FROM CourseLessonEntity cl WHERE cl.courseSection.id = :sectionId ORDER BY cl.orderIndex";
            return session.createQuery(hql, CourseLessonEntity.class)
                    .setParameter("sectionId", section.getId())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
