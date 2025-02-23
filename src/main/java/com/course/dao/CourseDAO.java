package com.course.dao;

import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CourseEntity;

import java.util.List;

public interface CourseDAO {
    CourseEntity createCourse(CourseEntity course);
    CourseEntity findById(Long id);
    PageResponse<CourseEntity> findByAccountCreatedId(Long id, CourseInstructorFilterRequest filterRequest);
    CourseEntity updateCourse(CourseEntity course);
}
