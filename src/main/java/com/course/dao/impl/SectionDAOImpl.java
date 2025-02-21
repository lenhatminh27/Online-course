package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.SectionDAO;
import com.course.entity.CourseEntity;
import com.course.entity.CourseSectionEntity;
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
            return session.get(CourseSectionEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CourseSectionEntity> findByCourse(CourseEntity course) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT cs FROM CourseSectionEntity cs WHERE cs.course.id = :courseId ORDER BY cs.orderIndex";
            return session.createQuery(hql, CourseSectionEntity.class)
                    .setParameter("courseId", course.getId())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public boolean existTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(c) FROM CourseSectionEntity c WHERE c.title = :title";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("title", title)
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



}
