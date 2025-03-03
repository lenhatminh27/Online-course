package com.course.dao.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.RatingDAO;
import com.course.entity.RatingEntity;
import com.course.common.utils.HibernateUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.course.common.utils.StringUtils.deAccent;

@Repository
public class RatingDaoImpl implements RatingDAO {

    @Override
    @Transactional
    public Double calRatingByCourseId(Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Câu truy vấn HQL để tính điểm trung bình rating của khóa học
            String hql = "SELECT AVG(r.rating) FROM RatingEntity r WHERE r.course.id = :courseId";

            // Thực thi truy vấn và nhận kết quả
            Double averageRating = (Double) session.createQuery(hql)
                    .setParameter("courseId", courseId)
                    .getSingleResult();

            // Nếu có giá trị trung bình rating, trả về, nếu không trả về 0.0
            return averageRating != null ? averageRating : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tính điểm rating của khóa học", e);
        }
    }

    @Override
    public List<RatingEntity> getAllRating() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM RatingEntity";
            return session.createQuery(hql, RatingEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<RatingEntity> getRatingByCourseId(long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM RatingEntity r WHERE r.course.id = :courseId ORDER BY r.createdAt DESC";
            List<RatingEntity> list = session.createQuery(hql, RatingEntity.class).setParameter("courseId", courseId).getResultList();
            list.forEach(rating -> {
                Hibernate.initialize(rating.getCourse());
                Hibernate.initialize(rating.getCourse().getAccountCreated());
                Hibernate.initialize(rating.getCourse().getCategories());
                Hibernate.initialize(rating.getAccount());
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public RatingEntity createRating(RatingEntity rating) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(rating);
            session.getTransaction().commit();
            return rating;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RatingEntity updateRating(RatingEntity rating) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(rating);
            session.getTransaction().commit();
            Hibernate.initialize(rating.getCourse());
            Hibernate.initialize(rating.getCourse().getAccountCreated());
            Hibernate.initialize(rating.getAccount());
            Hibernate.initialize(rating.getCourse().getCategories());
            return rating;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteRatingByRatingId(long ratingId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            RatingEntity ratingEntity = session.get(RatingEntity.class, ratingId);
            if (ratingEntity != null) {
                session.delete(ratingEntity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RatingEntity getRatingByRatingId(long ratingId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(RatingEntity.class, ratingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean isExistRating(long courseId, long accountId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(r) FROM RatingEntity r WHERE r.course.id = :courseId AND r.account.id = :accountId";
            Long count = session.createQuery(hql, Long.class).setParameter("courseId", courseId).setParameter("accountId", accountId).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RatingEntity> getTop3Rating(long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM RatingEntity r WHERE r.course.id = :courseId ORDER BY r.rating DESC, r.createdAt DESC";
            List<RatingEntity> list = session.createQuery(hql, RatingEntity.class)
                    .setParameter("courseId", courseId)
                    .setMaxResults(3) // Giới hạn kết quả là 3 đánh giá hàng đầu
                    .getResultList();

            // Khởi tạo các đối tượng liên kết để tránh LazyInitializationException
            list.forEach(rating -> {
                Hibernate.initialize(rating.getCourse());
                Hibernate.initialize(rating.getCourse().getAccountCreated());
                Hibernate.initialize(rating.getAccount());
                Hibernate.initialize(rating.getCourse().getCategories());
            });

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RatingEntity> findByReviewAndRatingAndCourseId(String review, int rating, Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Câu truy vấn HQL cơ bản để tìm kiếm theo rating và courseId
            String hql = "FROM RatingEntity r WHERE r.rating = :rating AND r.course.id = :courseId";

            // Tạo map để chứa các tham số
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("rating", rating);
            parameters.put("courseId", courseId);

            if (!ObjectUtils.isEmpty(review)) {
                String reviewSearch = deAccent(review);
                hql += " AND deAccent(r.review) LIKE :review";
                parameters.put("review", "%" + reviewSearch + "%");
            }

            Query<RatingEntity> query = session.createQuery(hql, RatingEntity.class);

            parameters.forEach(query::setParameter);

            List<RatingEntity> results = query.getResultList();

            // Khởi tạo các đối tượng liên quan (tránh LazyInitializationException)
            results.forEach(ratingEntity -> {
                Hibernate.initialize(ratingEntity.getCourse());
                Hibernate.initialize(ratingEntity.getCourse().getAccountCreated());
                Hibernate.initialize(ratingEntity.getAccount());
                Hibernate.initialize(ratingEntity.getCourse().getCategories());
            });

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Trả về danh sách rỗng nếu có lỗi
        }
    }

}


