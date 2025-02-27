package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.TransactionDAO;
import com.course.entity.TransactionEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Repository
public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public TransactionEntity saveTransaction(TransactionEntity transactionEntity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(transactionEntity);
            transaction.commit();
            return transactionEntity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public TransactionEntity findByTransactionDescription(String desc) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            TransactionEntity transaction = session.createQuery("FROM TransactionEntity t WHERE t.transactionDescription = :desc", TransactionEntity.class)
                    .setParameter("desc", desc)
                    .uniqueResult();
            if (transaction != null){
                Hibernate.initialize(transaction.getAccount());
            }
            return transaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TransactionEntity update(TransactionEntity transactionEntity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(transactionEntity);
            transaction.commit();
            return transactionEntity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }


}
