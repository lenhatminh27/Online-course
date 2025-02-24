package com.course.dao;

import com.course.entity.CourseEntity;
import com.course.entity.CourseSectionEntity;

import java.util.List;

public interface SectionDAO {

    CourseSectionEntity createSection(CourseSectionEntity section);

    CourseSectionEntity updateSection(CourseSectionEntity section);

    CourseSectionEntity findById(Long id);

    List<CourseSectionEntity> findByCourse(CourseEntity course);

    List<CourseSectionEntity> findByCourseNotIn(List<Long> sectionIds, CourseEntity course);

    boolean existTitle(String title, Long courseId);

    int countSectionsByCourse(CourseEntity course);

    List<CourseSectionEntity> searchSectionsInCourse(Long courseId, String articleContent);
}
