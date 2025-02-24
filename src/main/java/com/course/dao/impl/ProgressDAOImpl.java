package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.ProgressDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Repository
public class ProgressDAOImpl implements ProgressDAO {
    @Override
    public void deleteProgressByLessonId(Long lessonId) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String hql = "DELETE FROM ProgressEntity p WHERE p.courseLesson.id = :lessonId";
            session.createQuery(hql).setParameter("lessonId", lessonId).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to delete progress", e);
        }
    }

}
