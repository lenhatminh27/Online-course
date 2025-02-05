package com.course.dao;

import com.course.entity.BlogStatisticEntity;

public interface BlogStatisticDAO {

    BlogStatisticEntity createBlogStatistic(BlogStatisticEntity blogStatistic);

    BlogStatisticEntity updateBlogStatistic(BlogStatisticEntity blogStatistic);

    BlogStatisticEntity findById(Long id);
}
