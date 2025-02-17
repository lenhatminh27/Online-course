package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.PasswordResetTokenDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.entity.AccountEntity;
import com.course.entity.PasswordResetTokenEntity;
import com.course.entity.RefreshTokenEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;

@Repository
public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {


    @Override
    public void save(PasswordResetTokenEntity token) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(token);  // Lưu token vào DB
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save reset token", e);
        }
    }

    @Override
    public PasswordResetTokenEntity findByToken(String token) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM PasswordResetTokenEntity p WHERE p.token = :token";
            Query<PasswordResetTokenEntity> query = session.createQuery(hql, PasswordResetTokenEntity.class);
            query.setParameter("token", token);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find token by value", e);
        }
    }

    @Override
    public void delete(PasswordResetTokenEntity token) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(token);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete reset token", e);
        }
    }

    @Override
    public boolean existsByToken(String token) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(p) FROM PasswordResetTokenEntity p WHERE p.token = :token";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("token", token);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check if token exists", e);
        }
    }
}
