package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.AccountProfileDAO;
import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;
import com.course.security.context.AuthenticationContextHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Repository
public class AccountProfileDAOImpl implements AccountProfileDAO {




    @Override
    public AccountProfileEntity updateAccountProfile(AccountProfileEntity accountProfile) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(accountProfile);
            transaction.commit();
            return accountProfile;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save RefreshTokenEntity", e);
        }
    }
}
