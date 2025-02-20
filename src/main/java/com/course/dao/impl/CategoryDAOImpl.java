package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.CategoryDAO;
import com.course.entity.CategoriesEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public List<CategoriesEntity> getAllParentCategory() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CategoriesEntity c WHERE  c.parentCategories IS NULL ORDER BY c.createdAt DESC";
            return session.createQuery(hql, CategoriesEntity.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public CategoriesEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CategoriesEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CategoriesEntity createCategory(CategoriesEntity category) {
        Transaction transaction = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(category);
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo mới Category", e);
        }
    }


    @Override
    public boolean existCategory (String categoryName) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            String hql = "SELECT COUNT(c) FROM CategoriesEntity c WHERE c.name=:categoryName";
            Long count = (Long) session.createQuery(hql).setParameter("categoryName", categoryName).uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<CategoriesEntity> findAllChildrenCategories(Long parentId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CategoriesEntity c WHERE c.parentCategories.id = :id";
            return session.createQuery(hql, CategoriesEntity.class)
                    .setParameter("id", parentId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}



