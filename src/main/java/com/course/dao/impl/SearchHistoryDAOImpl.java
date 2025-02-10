package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.SearchHistoryDAO;
import com.course.entity.SearchHistoryEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SearchHistoryDAOImpl implements SearchHistoryDAO {

    @Override
    public void save(SearchHistoryEntity searchHistory) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(searchHistory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getRecentSearches(String email, int limit) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT s.content FROM SearchHistoryEntity s WHERE s.account.email = :email ORDER BY s.createdAt DESC";
            return session.createQuery(hql, String.class)
                    .setParameter("email", email)
                    .setMaxResults(limit)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

