package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.core.bean.annotations.Repository;
import com.course.dao.BookmarksBlogDAO;
import com.course.entity.BookmarksBlogEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Repository
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
            String hql = "DELETE FROM BookmarksBlogEntity b WHERE b.blog.id = :blogId";
            transaction = session.beginTransaction();
            session.createQuery(hql).setParameter("blogId", blogId).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BookmarksBlogEntity> getBookmarkedBlogsByUserId(Long accountId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT b FROM BookmarksBlogEntity b " +
                    "JOIN FETCH b.blog bl " +  // Load luôn blog
                    "JOIN FETCH bl.account acc " + // Load luôn account của blog
                    "WHERE b.account.id = :userId";
            return session.createQuery(hql, BookmarksBlogEntity.class).setParameter("userId", accountId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi không lấy được các trang đã đánh dấu", e);
        }
    }

    @Override
    public BookmarksBlogEntity findByBlogIdAndUserId(Long blogId, Long accountId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT b FROM BookmarksBlogEntity b WHERE b.blog.id = :blogId AND b.account.id = :accountId";
            return session.createQuery(hql, BookmarksBlogEntity.class)
                    .setParameter("blogId", blogId)
                    .setParameter("accountId", accountId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi không lấy được bookmark theo blogId", e);
        }
    }

    @Override
    public void deleteBookmarksBlogByBlogIdAndUserId(Long blogId, Long accountId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM BookmarksBlogEntity b WHERE b.blog.id = :blogId AND b.account.id = :accountId";
            int deletedRows = session.createQuery(hql)
                    .setParameter("blogId", blogId)
                    .setParameter("accountId", accountId)
                    .executeUpdate();
            transaction.commit();
            if (deletedRows == 0) {
                System.out.println("Không tìm thấy bookmark để xóa!");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa bookmark", e);
        }
    }
}