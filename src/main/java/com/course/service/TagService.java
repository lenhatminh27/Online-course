package com.course.service;

import com.course.dto.response.TagResponse;

import java.util.List;

public interface TagService {
    List<TagResponse> findTagsRecent();
}
