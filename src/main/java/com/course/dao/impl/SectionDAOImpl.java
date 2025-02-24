package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.SectionDAO;
import com.course.entity.CourseEntity;
import com.course.entity.CourseSectionEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
public class SectionDAOImpl implements SectionDAO {

    @Override
    public CourseSectionEntity createSection(CourseSectionEntity section) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(section);
            transaction.commit();
            return section;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public CourseSectionEntity updateSection(CourseSectionEntity section) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(section);
            transaction.commit();
            return section;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public CourseSectionEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CourseSectionEntity section = session.get(CourseSectionEntity.class, id);
            Hibernate.initialize(section.getCourse());
            Hibernate.initialize(section.getCourse().getAccountCreated());
            return section;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CourseSectionEntity> findByCourse(CourseEntity course) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT cs FROM CourseSectionEntity cs WHERE cs.course.id = :courseId ORDER BY cs.orderIndex";
            List<CourseSectionEntity> list = session.createQuery(hql, CourseSectionEntity.class)
                    .setParameter("courseId", course.getId())
                    .getResultList();
            list.forEach(section -> {
                Hibernate.initialize(section.getCourse());
                Hibernate.initialize(section.getCourse().getAccountCreated());
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseSectionEntity> findByCourseNotIn(List<Long> sectionIds, CourseEntity course) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT cs FROM CourseSectionEntity cs WHERE cs.id NOT IN :sectionIds AND cs.course.id = :courseId  ORDER BY cs.orderIndex";
            List<CourseSectionEntity> list = session.createQuery(hql, CourseSectionEntity.class)
                    .setParameter("sectionIds", sectionIds)
                    .setParameter("courseId", course.getId())
                    .getResultList();
            list.forEach(section -> {
                Hibernate.initialize(section.getCourse());
                Hibernate.initialize(section.getCourse().getAccountCreated());
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean existTitle(String title, Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(c) FROM CourseSectionEntity c WHERE c.title = :title AND c.course.id = :courseId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("title", title)
                    .setParameter("courseId", courseId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check title existence", e);
        }
    }


    @Override
    public int countSectionsByCourse(CourseEntity course) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(cs) FROM CourseSectionEntity cs WHERE cs.course.id = :courseId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("courseId", course.getId())
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<CourseSectionEntity> searchSectionsInCourse(Long courseId, String articleContent) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CourseSectionEntity s WHERE s.course.id = :course AND s.title LIKE :articleContent";
            List<CourseSectionEntity> list = session.createQuery(hql, CourseSectionEntity.class)
                    .setParameter("course", courseId)
                    .setParameter("articleContent", "%" + articleContent + "%")
                    .getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteSectionById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM CourseSectionEntity cs WHERE cs.id = :id";
            int deletedCount = session.createQuery(hql)
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete section with ID: " + id, e);
        }
    }

}
