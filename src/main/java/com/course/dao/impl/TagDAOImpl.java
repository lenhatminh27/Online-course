package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.TagDAO;
import com.course.entity.TagEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {
    @Override
    public List<TagEntity> findAllByTagName(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM TagEntity t WHERE t.name IN :tagNames";
            return session.createQuery(hql, TagEntity.class)
                    .setParameter("tagNames", tagNames)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch tags by names", e);
        }
    }


    @Override
    public List<TagEntity> saveAll(List<TagEntity> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (TagEntity tag : tags) {
                session.saveOrUpdate(tag);
            }
            transaction.commit();
            return tags;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save all tags", e);
        }
    }

    @Override
    public List<TagEntity> findTagsRecent() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM TagEntity t ORDER BY t.name ASC";
            return session.createQuery(hql, TagEntity.class)
                    .setMaxResults(10)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch tags by names", e);
        }
    }
}


