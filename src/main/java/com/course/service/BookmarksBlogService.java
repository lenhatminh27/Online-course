package com.course.service;
import com.course.dto.request.BookmarksBlogRequest;import com.course.dto.response.BookmarksBlogResponse;
import java.util.List;

public interface BookmarksBlogService {
    void createBookmarksBlog(BookmarksBlogRequest bookmarksBlogRequest);

    List<BookmarksBlogResponse> getBookmarkedBlogs();

    void deleteBookmark(Long blogId);
}