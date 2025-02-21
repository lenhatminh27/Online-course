package com.course.dao;

import com.course.entity.CourseEntity;
import com.course.entity.CourseSectionEntity;

import java.util.List;

public interface SectionDAO {

    CourseSectionEntity createSection(CourseSectionEntity section);

    CourseSectionEntity updateSection(CourseSectionEntity section);

    CourseSectionEntity findById(Long id);

    List<CourseSectionEntity> findByCourse(CourseEntity course);

    boolean existTitle(String title);

    int countSectionsByCourse(CourseEntity course);
}
