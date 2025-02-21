package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.CourseLessonDAO;
import com.course.entity.BlogEntity;
import com.course.entity.CourseLessonEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;

public class CourseLessonDAOImpl implements CourseLessonDAO {

    @Override
    public CourseLessonEntity findCourseLessonById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CourseLessonEntity b WHERE b.id = :id";
            CourseLessonEntity lesson = session.createQuery(hql, CourseLessonEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return lesson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
