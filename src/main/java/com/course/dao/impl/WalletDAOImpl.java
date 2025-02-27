package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.WalletDAO;
import com.course.entity.WalletEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Repository
public class WalletDAOImpl implements WalletDAO {

    @Override
    public WalletEntity createWallet(WalletEntity wallet) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(wallet);
            transaction.commit();
            return wallet;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public WalletEntity getWallet(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(WalletEntity.class, id); // Tìm wallet theo ID
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch wallet", e);
        }
    }

    @Override
    public WalletEntity updateWallet(WalletEntity wallet) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(wallet);
            transaction.commit();
            return wallet;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save account", e);
        }
    }

}
