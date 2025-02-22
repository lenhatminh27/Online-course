package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.WishlistDAO;
import com.course.entity.AccountEntity;
import com.course.entity.CourseEntity;
import com.course.entity.WishlistEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.util.List;

public class WishlistDaoImpl implements WishlistDAO {
    @Override
    public WishlistEntity save(WishlistEntity wishlist) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(wishlist);
            transaction.commit();
            return wishlist;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lưu vào danh sách yêu thích thất bại!", e);
        }
    }

    @Override
    public void delete(WishlistEntity wishlist) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(wishlist);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Xóa khỏi danh sách yêu thích thất bại!", e);
        }
    }

    @Override
    public boolean checkCourseExistInWishlist(Long course_id, Long account_id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(w) FROM WishlistEntity w WHERE w.course.id = :course_id AND w.account.id = :account_id";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("course_id", course_id)
                    .setParameter("account_id", account_id)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi kiểm tra khóa học đã tồn tại trong danh sách yêu thích", e);
        }
    }

    @Override
    public WishlistEntity findWishlistById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Viết câu truy vấn HQL tìm kiếm khóa học theo id
            String hql = "FROM WishlistEntity w WHERE w.id = :id";
            // Truy vấn khóa học theo id và lấy kết quả duy nhất
            WishlistEntity wishlist = session.createQuery(hql, WishlistEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            // Kiểm tra nếu không tìm thấy khóa học
            return wishlist;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
            throw new RuntimeException("Lỗi khi tìm khóa học theo ID", e);
        }
    }


    @Transactional
    @Override
    public List<WishlistEntity> getByUserId(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM WishlistEntity w " +
                    "JOIN FETCH w.course c " +
                    "JOIN FETCH w.account a " +
                    "WHERE w.account.id = :id";
            return session.createQuery(hql, WishlistEntity.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách yêu thích của người dùng", e);
        }
    }


}
