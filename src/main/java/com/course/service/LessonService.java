package com.course.service;


import com.course.dto.request.CreateLessonRequest;
import com.course.dto.request.UpdateLessonRequest;
import com.course.dto.response.LessonResponse;

public interface LessonService {
    LessonResponse createLesson(CreateLessonRequest createLessonRequest);

    LessonResponse updateLesson(UpdateLessonRequest updateLessonRequest);

    void deleteVideo(Long lessonId);

    LessonResponse getLessonById(Long lessonId);

    void deleteLesson(Long lessonId);
}
