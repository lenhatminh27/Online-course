package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.common.utils.ObjectUtils;
import com.course.core.repository.specification.HibernateQueryHelper;
import com.course.dao.BlogDAO;
import com.course.dto.request.BlogFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.BlogCommentEntity;
import com.course.entity.BlogEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.course.common.utils.StringUtils.deAccent;

public class BlogDAOImpl implements BlogDAO {

    @Override
    public BlogEntity createBlog(BlogEntity blog) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(blog);
            transaction.commit();
            return blog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }


    @Override
    public boolean existsSlug(String slug) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM BlogEntity b WHERE b.slug = :slug";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("slug", slug)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check slug existence", e);
        }
    }

    @Override
    public boolean existTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT 1 FROM BlogEntity b WHERE b.title = :title";
            Object result = session.createQuery(hql)
                    .setParameter("title", title)
                    .uniqueResult();
            return result != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find blog by title", e);
        }
    }

    @Override
    public PageResponse<BlogEntity> getBlogsByPage(BlogFilterRequest blogFilter) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String baseHql = "FROM BlogEntity b JOIN FETCH b.account a WHERE 1=1";
            Map<String, Object> parameters = new HashMap<>();

            if (!ObjectUtils.isEmpty(blogFilter.getSearch())) {
                String searchString = deAccent(blogFilter.getSearch());
                baseHql += " AND (deAccent(b.title) LIKE :search OR deAccent(b.content) LIKE :search OR deAccent(a.email) LIKE :search)";
                parameters.put("search", "%" + searchString + "%");
            }

            if (!ObjectUtils.isEmpty(blogFilter.getTags())) {
                baseHql += " AND EXISTS (SELECT 1 FROM b.tags t WHERE t.name IN (:tags))";
                parameters.put("tags", blogFilter.getTags());
            }
            Query<BlogEntity> query = HibernateQueryHelper.buildQuery(
                    session,
                    baseHql,
                    parameters,
                    blogFilter.getSort(),
                    blogFilter.getPageRequest(blogFilter.getSort()),
                    BlogEntity.class
            );
            List<BlogEntity> blogs = query.getResultList();
            blogs.forEach(blog -> {
                Hibernate.initialize(blog.getTags());
                Hibernate.initialize(blog.getAccount());
                Hibernate.initialize(blog.getBlogStatistic());
            });
            Query<Long> countQuery = HibernateQueryHelper.buildCountQuery(session, baseHql, parameters, "b");
            long totalElements = countQuery.uniqueResult();
            int totalPages = (int) Math.ceil((double) totalElements / blogFilter.getSize());

            return new PageResponse<>(blogFilter.getPage(), totalPages, blogs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch blogs by page", e);
        }
    }


    @Override
    public List<BlogEntity> getTopBlogsRecent() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogEntity b ORDER BY b.createAt DESC";
            Query<BlogEntity> query = session.createQuery(hql, BlogEntity.class)
                    .setMaxResults(4);
            List<BlogEntity> blogs = query.getResultList();
            blogs.forEach(blog -> {
                Hibernate.initialize(blog.getTags());
                Hibernate.initialize(blog.getAccount());
                Hibernate.initialize(blog.getBlogStatistic());
            });;
            return blogs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch top recent blogs", e);
        }
    }

    @Override
    public BlogEntity findBlogById(long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogEntity b WHERE b.id = :id";
            BlogEntity blog = session.createQuery(hql, BlogEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            Hibernate.initialize(blog.getTags());
            Hibernate.initialize(blog.getAccount());
            Hibernate.initialize(blog.getBlogStatistic());
            return blog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BlogEntity findBlogBySlug(String slug) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM BlogEntity b WHERE b.slug = :slug";
            Query<BlogEntity> query = session.createQuery(hql, BlogEntity.class)
                    .setParameter("slug", slug);
            BlogEntity blog = query.uniqueResult();
            Hibernate.initialize(blog.getTags());
            Hibernate.initialize(blog.getAccount());
            Hibernate.initialize(blog.getBlogStatistic());
            return blog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
