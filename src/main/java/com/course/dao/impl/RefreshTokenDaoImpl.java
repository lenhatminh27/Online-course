package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.RefreshTokenDAO;
import com.course.entity.AccountEntity;
import com.course.entity.RefreshTokenEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Repository
public class RefreshTokenDaoImpl implements RefreshTokenDAO {
    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshToken) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(refreshToken);
            transaction.commit();
            return refreshToken;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save RefreshTokenEntity", e);
        }
    }

    @Override
    public RefreshTokenEntity update(RefreshTokenEntity refreshToken) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(refreshToken);
            transaction.commit();
            return refreshToken;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save RefreshTokenEntity", e);
        }
    }

    @Override
    public List<RefreshTokenEntity> findByAccountAndRevoked(AccountEntity account, boolean isRevoked) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM RefreshTokenEntity rt WHERE rt.account = :account AND rt.revoked = :revoked",
                            RefreshTokenEntity.class
                    )
                    .setParameter("account", account)
                    .setParameter("revoked", isRevoked)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch refresh tokens", e);
        }
    }

    @Override
    public void saveAll(List<RefreshTokenEntity> listRefreshTokenAccount) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (RefreshTokenEntity refreshToken : listRefreshTokenAccount) {
                session.merge(refreshToken);//Update
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update all RefreshTokenEntity", e);
        }
    }

    @Override
    public RefreshTokenEntity findByRefreshTokenAnsRevoked(String refreshToken, boolean isRevoked) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM RefreshTokenEntity rt WHERE rt.refreshToken = :refreshToken AND rt.revoked = :revoked",
                            RefreshTokenEntity.class
                    )
                    .setParameter("refreshToken", refreshToken)
                    .setParameter("revoked", isRevoked)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch refresh token", e);
        }
    }
}
