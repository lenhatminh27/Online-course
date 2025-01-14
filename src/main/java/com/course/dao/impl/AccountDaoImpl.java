package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.AccountDAO;
import com.course.entity.AccountEntity;
import org.hibernate.Session;

public class AccountDaoImpl implements AccountDAO {

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()){
            String hql = "SELECT COUNT(a) FROM AccountEntity a WHERE a.email = :email";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
            return count > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AccountEntity findByEmail(String email) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()){
            String hql = "FROM AccountEntity a WHERE a.email = :email";
            return session.createQuery(hql, AccountEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
