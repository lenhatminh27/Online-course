package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.EnrollmentDAO;
import com.course.entity.CourseEntity;
import com.course.entity.EnrollmentEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.transaction.Transactional;
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

    @Override
    public Long countByCourseId(Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.course.id = :courseId", Long.class)
                    .setParameter("courseId", courseId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    @Transactional
    public List<CourseEntity> getEnrollmentCourseByAccountId(Long accountId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {

            // Truy vấn danh sách khóa học đã đăng ký của tài khoản
            List<CourseEntity> courses = session.createQuery(
                            "SELECT DISTINCT c FROM EnrollmentEntity e " +
                                    "JOIN e.course c " +
                                    "LEFT JOIN FETCH c.accountCreated " +
                                    "LEFT JOIN FETCH c.categories " +
                                    "WHERE e.account.id = :accountId", CourseEntity.class)
                    .setParameter("accountId", accountId)
                    .getResultList();

            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
