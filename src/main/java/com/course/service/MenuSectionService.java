package com.course.service;

import com.course.dto.response.MenuSectionResponse;

import java.util.List;

public interface MenuSectionService {
    List<MenuSectionResponse> getMenuSectionByCourseId(Long courseId);

    List<MenuSectionResponse> getMenuSectionByCourseIdPublic(Long courseId);

}
