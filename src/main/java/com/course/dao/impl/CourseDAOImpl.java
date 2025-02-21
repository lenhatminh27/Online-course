package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.CourseDAO;
import com.course.entity.CourseEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @Override
    public CourseEntity createCourse(CourseEntity course) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public CourseEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CourseEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CourseEntity> findByAccountCreatedId(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<CourseEntity> courses = session.createQuery("FROM CourseEntity c WHERE c.accountCreated.id = :accountId", CourseEntity.class)
                    .setParameter("accountId", id)
                    .getResultList();
            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Trả về danh sách rỗng nếu lỗi
        }
    }


}
