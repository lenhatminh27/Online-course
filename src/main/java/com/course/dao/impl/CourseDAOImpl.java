package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Repository;
import com.course.core.repository.specification.HibernateQueryHelper;
import com.course.dao.CourseDAO;
import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CourseEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.course.common.utils.StringUtils.deAccent;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @Override
    public CourseEntity createCourse(CourseEntity course) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }

    @Override
    public CourseEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CourseEntity course = session.get(CourseEntity.class, id);
            Hibernate.initialize(course.getAccountCreated());
            Hibernate.initialize(course.getCategories());
            return course;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResponse<CourseEntity> findByAccountCreatedId(Long id, CourseInstructorFilterRequest filterRequest) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String baseHql = "FROM CourseEntity c WHERE 1=1 ";
            Map<String, Object> parameters = new HashMap<>();
            if (!ObjectUtils.isEmpty(filterRequest.getSearch())) {
                String searchString = deAccent(filterRequest.getSearch());
                baseHql += " AND (deAccent(c.title) LIKE :search OR deAccent(c.description) LIKE :search) ";
                parameters.put("search", "%" + searchString + "%");
            }

            if (!ObjectUtils.isEmpty(id)) {
                baseHql += " AND c.accountCreated.id = :accountId";
                parameters.put("accountId", id);
            }
            Query<CourseEntity> query = HibernateQueryHelper.buildQuery(
                    session,
                    baseHql,
                    parameters,
                    filterRequest.getSort(),
                    filterRequest.getPageRequest(filterRequest.getSort()),
                    CourseEntity.class
            );
            List<CourseEntity> courses = query.getResultList();
            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });
            Query<Long> countQuery = HibernateQueryHelper.buildCountQuery(session, baseHql, parameters, null);
            long totalElements = countQuery.uniqueResult();
            int totalPages = (int) Math.ceil((double) totalElements / filterRequest.getSize());
            return new PageResponse<>(filterRequest.getPage(), totalPages, courses);
        }catch (Exception e) {
            throw new RuntimeException("Failed to fetch blogs by page", e);
        }
    }



    @Override
    public CourseEntity updateCourse(CourseEntity course) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create blog", e);
        }
    }


}
