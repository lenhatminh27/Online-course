package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.CategoryDAO;
import com.course.entity.BlogCommentEntity;
import com.course.entity.CategoriesEntity;
import org.hibernate.Session;

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

}
