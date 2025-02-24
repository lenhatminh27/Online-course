package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.EnrollmentDAO;
import com.course.entity.CourseEntity;
import com.course.entity.EnrollmentEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

@Repository
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public boolean getEnrollmentByAccountIdAndCourseId(Long accountId, Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            EnrollmentEntity enrollment = session.createQuery("FROM EnrollmentEntity e WHERE e.account.id = :accountId AND e.course.id = :courseId", EnrollmentEntity.class)
                    .setParameter("accountId", accountId)
                    .setParameter("courseId", courseId)
                    .uniqueResult();
            return enrollment != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
