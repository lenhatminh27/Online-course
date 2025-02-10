package com.course.dao;

import com.course.entity.BookmarksBlogEntity;

public interface BookmarksBlogDAO {
    BookmarksBlogEntity createBookmarksBlog(BookmarksBlogEntity bookmarksBlog);
    boolean existsBookmarkBlogId(Long blogId, Long accountId);
    void save(BookmarksBlogEntity bookmarksBlogEntity);
    void deleteAllBookmarksBlogByBlogId(Long blogId);
}
