package com.course.service;

import java.util.List;
import com.course.dto.request.BlogCommentCreateRequest;
import com.course.dto.request.BlogCommentUpdateRequest;
import com.course.dto.response.BlogCommentResponse;

public interface BlogCommentService {
    BlogCommentResponse createBlogComment(BlogCommentCreateRequest blogCommentCreateRequest);

    BlogCommentResponse updateBlogComment(BlogCommentUpdateRequest blogCommentUpdateRequest);

    List<BlogCommentResponse> getListCommentByBlogSlug(String slug);
}
