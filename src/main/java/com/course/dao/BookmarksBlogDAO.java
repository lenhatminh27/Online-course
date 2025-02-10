package com.course.dao;
import com.course.entity.BookmarksBlogEntity;
import java.util.List;

public interface BookmarksBlogDAO {
    BookmarksBlogEntity createBookmarksBlog(BookmarksBlogEntity bookmarksBlog);
    boolean existsBookmarkBlogId(Long blogId, Long accountId);
    void save(BookmarksBlogEntity bookmarksBlogEntity);
    void deleteAllBookmarksBlogByBlogId(Long blogId);
    List<BookmarksBlogEntity> getBookmarkedBlogsByUserId(Long accountId);
    BookmarksBlogEntity findByBlogIdAndUserId(Long blogId, Long accountId);
    void deleteBookmarksBlogByBlogIdAndUserId(Long blogId, Long accountId);
}