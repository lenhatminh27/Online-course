package com.course.dao;

import com.course.dto.request.BlogFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.BlogCommentEntity;
import com.course.entity.BlogEntity;

import java.util.List;

public interface BlogDAO {
    BlogEntity createBlog(BlogEntity blog);

    boolean existsSlug(String slug);

    boolean existTitle(String title);

    PageResponse<BlogEntity> getBlogsByPage(BlogFilterRequest blogFilter);

    List<BlogEntity> getTopBlogsRecent();

    BlogEntity findBlogById(long id);

    BlogEntity findBlogBySlug(String slug);

    void updateBlog(BlogEntity blog);
}

