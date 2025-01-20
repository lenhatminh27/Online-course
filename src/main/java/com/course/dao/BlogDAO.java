package com.course.dao;

import com.course.entity.BlogEntity;

public interface BlogDAO {
    BlogEntity createBlog(BlogEntity blog);

    boolean existsSlug(String slug);

    boolean existTitle(String title);
}

