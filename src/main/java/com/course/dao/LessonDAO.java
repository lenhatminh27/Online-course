package com.course.dao;

import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;

import java.util.List;

public interface LessonDAO {
    CourseLessonEntity createLesson(CourseLessonEntity lesson);

    CourseLessonEntity updateLesson(CourseLessonEntity lesson);

    boolean existsByTitle(String title, Long sectionId);

    CourseLessonEntity findById(Long id);

    int countExistSection(CourseSectionEntity section);

    List<CourseLessonEntity> findBySection(CourseSectionEntity section);

    void deleteLesson(Long lessonId);
}
