package com.course.service;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.response.BlogResponse;
import com.course.entity.BlogEntity;

public interface BlogService {
    BlogResponse createBlog(BlogCreateRequest blogCreateRequest);
    boolean existTitle(String title);
}
