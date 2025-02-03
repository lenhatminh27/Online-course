package com.course.dao;

import com.course.entity.TagEntity;

import java.util.List;

public interface TagDAO {

    List<TagEntity> findAllByTagName(List<String> tagsName);

    List<TagEntity> saveAll(List<TagEntity> tags);

    List<TagEntity> findTagsRecent();
}

