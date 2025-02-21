package com.course.service;

import com.course.dto.request.CreateSectionRequest;
import com.course.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {
    SectionResponse creatSection(CreateSectionRequest section);
    List<SectionResponse> getSectionByCourseId(Long courseId);
}
