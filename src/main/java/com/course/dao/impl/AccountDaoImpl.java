package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.AccountDAO;
import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Repository
public class AccountDaoImpl implements AccountDAO {

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(a) FROM AccountEntity a WHERE a.email = :email";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AccountEntity findByEmail(String email) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM AccountEntity a WHERE a.email = :email";
            return session.createQuery(hql, AccountEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(account);
            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public AccountEntity update(AccountEntity account) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(account);
            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public AccountProfileEntity saveAccountProfile(AccountProfileEntity accountProfile) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(accountProfile);
            transaction.commit();
            return accountProfile;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save account", e);
        }
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Regex để kiểm tra email chuẩn xác hơn
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }


}
