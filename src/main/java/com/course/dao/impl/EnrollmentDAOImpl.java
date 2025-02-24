package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.EnrollmentDAO;
import org.hibernate.Session;

@Repository
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public boolean getEnrollmentByAccountIdAndCourseId(Long accountId, Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.account.id = :accountId AND e.course.id = :courseId",
                            Long.class)
                    .setParameter("accountId", accountId)
                    .setParameter("courseId", courseId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

