package com.course.dao;

import com.course.entity.CourseEntity;

import java.util.List;

public interface CourseDAO {
    CourseEntity createCourse(CourseEntity course);
    CourseEntity findById(Long id);
    List<CourseEntity> findByAccountCreatedId(Long id);
}
