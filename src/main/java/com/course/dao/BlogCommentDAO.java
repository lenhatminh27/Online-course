package com.course.dao;

import com.course.entity.BlogCommentEntity;

import java.util.List;

public interface BlogCommentDAO {

    BlogCommentEntity createBlogComment(BlogCommentEntity blogComment);

    BlogCommentEntity findBlogCommentById(Long id);

    List<BlogCommentEntity> findAllChildrenBlogComments(Long id);

    void updateBlogComment(BlogCommentEntity blogComment);

    Long findNumberCommentOfBlog(Long id);

    List<BlogCommentEntity> findListCommentByBlogSlug(String slug);

    void deleteBlogComment(BlogCommentEntity blogComment);
}
