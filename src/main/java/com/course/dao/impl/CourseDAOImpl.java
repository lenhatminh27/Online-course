package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.CourseDAO;
import com.course.entity.CourseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

}
