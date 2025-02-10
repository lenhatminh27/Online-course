package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.BookmarksBlogDAO;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.entity.BlogEntity;
import com.course.entity.BookmarksBlogEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BookmarksBlogDAOImpl implements BookmarksBlogDAO {

    @Override
    public BookmarksBlogEntity createBookmarksBlog(BookmarksBlogEntity bookmarksBlog) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookmarksBlog);
            transaction.commit();
            return bookmarksBlog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi không tạo được đánh dấu trang", e);
        }
    }

    @Override
    public boolean existsBookmarkBlogId(Long blogId, Long accountId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM BookmarksBlogEntity b WHERE b.blog.id = :blogId AND b.account.id = :accountId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("blogId", blogId)
                    .setParameter("accountId", accountId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi kiểm tra bài viết đã được đánh dấu chưa", e);
        }
    }

    @Override
    public void save(BookmarksBlogEntity bookmarksBlogEntity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookmarksBlogEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi không lưu được trang đã đánh dấu", e);
        }
    }

    @Override
    public void deleteAllBookmarksBlogByBlogId(Long blogId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            String hql = "DELETE FROM BookmarksBlogEntity b WHERE b.blogId = :blogId";
            transaction = session.beginTransaction();
            session.createQuery(hql).setParameter("blogId", blogId).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
