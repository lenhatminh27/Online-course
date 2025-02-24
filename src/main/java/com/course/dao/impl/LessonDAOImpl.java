package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.common.utils.StringUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.LessonDAO;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import org.hibernate.Hibernate;
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
    public boolean existsByTitle(String title, Long sectionId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(c) FROM CourseLessonEntity c WHERE c.title = :title AND c.courseSection.id = :sectionId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("title", title)
                    .setParameter("sectionId", sectionId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check title existence", e);
        }
    }


    public CourseLessonEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CourseLessonEntity courseLesson = session.get(CourseLessonEntity.class, id);
            Hibernate.initialize(courseLesson.getCourseSection());
            Hibernate.initialize(courseLesson.getCourseSection().getCourse());
            Hibernate.initialize(courseLesson.getCourseSection().getCourse().getAccountCreated());
            return courseLesson;
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
            List<CourseLessonEntity> list = session.createQuery(hql, CourseLessonEntity.class)
                    .setParameter("sectionId", section.getId())
                    .getResultList();
            list.forEach(lesson -> {
                Hibernate.initialize(lesson.getCourseSection());
                Hibernate.initialize(lesson.getCourseSection().getCourse());
                Hibernate.initialize(lesson.getCourseSection().getCourse().getAccountCreated());
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteLesson(Long lessonId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM CourseLessonEntity cl WHERE cl.id = :lessonId";
            int deletedCount = session.createQuery(hql)
                    .setParameter("lessonId", lessonId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete lesson with ID: " + lessonId, e);
        }
    }

    @Override
    public List<CourseLessonEntity> searchLessonsInCourse(List<Long> sectionIds, String articleContent) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CourseLessonEntity l WHERE l.courseSection.id IN :sectionIds AND (l.title LIKE :articleContent OR l.article LIKE :articleContent)";
            List<CourseLessonEntity> lessons = session.createQuery(hql, CourseLessonEntity.class)
                    .setParameter("sectionIds", sectionIds)
                    .setParameter("articleContent", "%" + articleContent + "%")
                    .getResultList();
            return lessons;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
