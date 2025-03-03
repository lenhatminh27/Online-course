package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.RatingDao;
import org.hibernate.Session;

import javax.transaction.Transactional;

@Repository
public class RatingDaoImpl implements RatingDao {

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

}
