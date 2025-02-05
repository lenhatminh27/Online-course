package com.course.dao;

import com.course.entity.BlogCommentEntity;

import java.util.List;

public interface BlogCommentDAO {

    BlogCommentEntity createBlogComment(BlogCommentEntity blogComment);

    BlogCommentEntity findBlogCommentById(Long id);

    List<BlogCommentEntity> findAllChildrenBlogComments(Long id);
}
