package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Repository;
import com.course.core.repository.specification.HibernateQueryHelper;
import com.course.dao.CategoryDAO;
import com.course.dto.request.CategoryFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CategoriesEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

import static com.course.common.utils.HibernateUtils.getSession;
import static com.course.common.utils.StringUtils.deAccent;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean isCategoryInUse(Long categoryId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM course_categories WHERE categories_id = :categoryId";
            Long count = (Long) session.createNativeQuery(hql)
                    .setParameter("categoryId", categoryId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PageResponse<CategoriesEntity> getCategoriesByPage(CategoryFilterRequest categoryFilter) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String baseHql = "FROM CategoriesEntity c LEFT JOIN FETCH c.parentCategories p WHERE 1=1";
            Map<String, Object> parameters = new HashMap<>();

            // Tìm kiếm theo tên category (không phân biệt dấu)
            if (!ObjectUtils.isEmpty(categoryFilter.getSearch())) {
                String searchString = deAccent(categoryFilter.getSearch());
                baseHql += " AND (deAccent(c.name) LIKE :search OR deAccent(c.description) LIKE :search)";
                parameters.put("search", "%" + searchString + "%");
            }

            // Xây dựng truy vấn chính để lấy danh sách categories
            Query<CategoriesEntity> query = HibernateQueryHelper.buildQuery(
                    session,
                    baseHql,
                    parameters,
                    categoryFilter.getSort(),
                    categoryFilter.getPageRequest(categoryFilter.getSort()),
                    CategoriesEntity.class
            );

            List<CategoriesEntity> categories = query.getResultList();

            // Khởi tạo các quan hệ nếu cần
            categories.forEach(category -> Hibernate.initialize(category.getParentCategories()));

            // Truy vấn đếm tổng số phần tử
            Query<Long> countQuery = HibernateQueryHelper.buildCountQuery(session, baseHql, parameters, "c");
            long totalElements = countQuery.uniqueResult();
            int totalPages = (int) Math.ceil((double) totalElements / categoryFilter.getSize());

            return new PageResponse<>(categoryFilter.getPage(), totalPages, categories);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch categories by page", e);
        }
    }


    @Override
    public List<CategoriesEntity> getAllCategories() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM CategoriesEntity ORDER BY createdAt DESC";
            return session.createQuery(hql, CategoriesEntity.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

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
            String hql = "SELECT c FROM CategoriesEntity c LEFT JOIN FETCH c.childrenCategories WHERE c.id = :id";
            return session.createQuery(hql, CategoriesEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CategoriesEntity findByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CategoriesEntity.class, name);
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
    public boolean isDuplicateCategoryName(Long categoryId, String categoryName) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(c) FROM CategoriesEntity c WHERE c.name = :categoryName AND c.id != :categoryId";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("categoryName", categoryName)
                    .setParameter("categoryId", categoryId)
                    .uniqueResult();
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

    @Override
    public CategoriesEntity updateCategory(CategoriesEntity category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(category);
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save RefreshTokenEntity", e);
        }
    }

    @Override
    public void deleteCategory(CategoriesEntity category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Xoá tất cả các category con trước
            List<CategoriesEntity> children = category.getChildrenCategories();
            for (CategoriesEntity child : children) {
                session.delete(child);
            }

            // Sau đó mới xoá category cha
            session.delete(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa category", e);
        }
    }
}



