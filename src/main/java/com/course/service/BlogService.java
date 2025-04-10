package com.course.service;

import com.course.dto.request.BlogCreateRequest;
import com.course.dto.request.BlogFilterRequest;
import com.course.dto.request.BlogUpdateRequest;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.PageResponse;

import java.util.List;

public interface BlogService {
    BlogResponse createBlog(BlogCreateRequest blogCreateRequest);
    boolean existTitle(String title);
    PageResponse<BlogResponse> getBlogs(BlogFilterRequest filterRequest);
    List<BlogResponse> getTopBlogRecent();
    void likeBlog(Long blogId);
    void deleteLikeBlog(Long blogId);
    BlogResponse findBlogBySlug(String slug);
    PageResponse<BlogResponse> getBlogsByInstructor(BlogFilterRequest filterRequest);
    void viewBlog(Long blogId);
    void updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest);
    void deleteBlog(Long blogId);
}
