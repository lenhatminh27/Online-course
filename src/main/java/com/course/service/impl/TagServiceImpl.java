package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.TagDAO;
import com.course.dto.response.TagResponse;
import com.course.entity.TagEntity;
import com.course.service.TagService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;


    @Override
    public List<TagResponse> findTagsRecent() {
        List<TagEntity> tagsRecent = tagDAO.findTagsRecent();
        return tagsRecent.stream()
                .map(it -> new TagResponse(it.getId(), it.getName()))
                .toList();
    }
}
