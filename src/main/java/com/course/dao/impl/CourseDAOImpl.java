package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.StringUtils;
import com.course.core.bean.annotations.Repository;
import com.course.core.repository.specification.HibernateQueryHelper;
import com.course.dao.CourseDAO;
import com.course.dto.request.CourseFilterRequest;
import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.request.ReviewCourseFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CourseEntity;
import com.course.entity.enums.CourseStatus;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.Collections;
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
        } catch (Exception e) {
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

    @Override
    public void deleteCourse(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM CourseEntity c WHERE c.id = :id";
            int deletedCount = session.createQuery(hql)
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete course with ID: " + id, e);
        }
    }


    @Override
    public void updateCourseStatus(Long id, CourseStatus status) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CourseEntity course = session.get(CourseEntity.class, id);
            if (course != null) {
                course.setStatus(status);
                session.update(course);
                transaction.commit();
            } else {
                throw new RuntimeException("Course not found with id: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update course status", e);
        }
    }


    @Override
    public List<CourseEntity> findByStatus(CourseStatus status) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<CourseEntity> courses = session.createQuery("FROM CourseEntity c WHERE c.status = :status", CourseEntity.class)
                    .setParameter("status", status) // Đảm bảo `status` là enum
                    .getResultList();
            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });
            for (CourseEntity course : courses) {
                System.out.println(course.toString());
            }
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public PageResponse<CourseEntity> getInReviewCourses(ReviewCourseFilterRequest filterRequest) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {

            String hql = "FROM CourseEntity c " +
                    "JOIN FETCH c.accountCreated ac " +
                    "LEFT JOIN FETCH c.categories cat " +
                    "WHERE c.status = :status";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("status", CourseStatus.IN_REVIEW);

            if (!ObjectUtils.isEmpty(filterRequest.getSearch())) {
                String searchString = StringUtils.deAccent(filterRequest.getSearch().toLowerCase());
                hql += " AND deAccent(LOWER(b.title)) LIKE :search";
                parameters.put("search", "%" + searchString + "%");
            }

            Query<CourseEntity> query = HibernateQueryHelper.buildQuery(
                    session,
                    hql,
                    parameters,
                    filterRequest.getSort(),
                    filterRequest.getPageRequest(filterRequest.getSort()),
                    CourseEntity.class
            );

            long totalElements = countCoursesInReview(session, filterRequest.getSearch());
            int totalPages = (int) Math.ceil((double) totalElements / filterRequest.getSize());
            query.setFirstResult((filterRequest.getPage() - 1) * filterRequest.getSize());
            query.setMaxResults(filterRequest.getSize());

            List<CourseEntity> courses = query.getResultList();
            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });

            return new PageResponse<>(filterRequest.getPage(), totalPages, courses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch courses by page", e);
        }
    }

    public long countCoursesInReview(Session session, String search) {
        String hql = "SELECT COUNT(c) FROM CourseEntity c WHERE c.status = :status";
        if (search != null && !search.isEmpty()) {
            hql += " AND c.title LIKE :search";
        }

        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("status", CourseStatus.IN_REVIEW);

        if (search != null && !search.isEmpty()) {
            query.setParameter("search", "%" + search + "%");
        }

        return query.uniqueResult();
    }

    @Override
    @Transactional
    public List<CourseEntity> getTop3Courses() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {

            List<CourseEntity> courses = session.createQuery(
                            "FROM CourseEntity c WHERE c.status = 'PUBLIC' ORDER BY c.id ASC", // Sắp xếp theo ID từ bé đến lớn
                            CourseEntity.class)
                    .setMaxResults(3) // Giới hạn chỉ lấy 3 khóa học
                    .getResultList();

            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });

            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    @Transactional
    public PageResponse<CourseEntity> getAllCourses(CourseFilterRequest filterRequest) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Câu truy vấn cơ bản: chỉ lấy các khóa học có trạng thái PUBLIC
            String baseHql = "FROM CourseEntity c WHERE c.status = 'PUBLIC'";
            Map<String, Object> parameters = new HashMap<>();

            // Lọc theo từ khóa tìm kiếm (tìm trong title, description, email của người tạo)
            if (!ObjectUtils.isEmpty(filterRequest.getSearch())) {
                String searchString = deAccent(filterRequest.getSearch());
                baseHql += " AND (deAccent(c.title) LIKE :search OR deAccent(c.description) LIKE :search OR deAccent(c.accountCreated.email) LIKE :search)";
                parameters.put("search", "%" + searchString + "%");
            }

            // Tạo truy vấn Hibernate với sắp xếp & phân trang
            Query<CourseEntity> query = HibernateQueryHelper.buildQuery(
                    session,
                    baseHql,
                    parameters,
                    filterRequest.getSort(),
                    filterRequest.getPageRequest(filterRequest.getSort()),
                    CourseEntity.class
            );

            // Lấy danh sách khóa học theo điều kiện
            List<CourseEntity> courses = query.getResultList();
            courses.forEach(course -> {
                Hibernate.initialize(course.getAccountCreated());
                Hibernate.initialize(course.getCategories());
            });

            // Truy vấn đếm tổng số bản ghi để tính tổng số trang
            Query<Long> countQuery = HibernateQueryHelper.buildCountQuery(session, baseHql, parameters, null);
            long totalElements = countQuery.uniqueResult();
            int totalPages = (int) Math.ceil((double) totalElements / filterRequest.getSize());

            // Trả về kết quả dưới dạng PageResponse
            return new PageResponse<>(filterRequest.getPage(), totalPages, courses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch public courses by page", e);
        }
    }





}
