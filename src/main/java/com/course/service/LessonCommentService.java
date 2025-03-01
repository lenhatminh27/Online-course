package com.course.service;

import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.request.UpdateLessonCommentRequest;
import com.course.dto.response.LessonCommentResponse;

import java.util.List;

public interface LessonCommentService {

    LessonCommentResponse createLessonComment(CreateLessonCommentRequest request);

    List<LessonCommentResponse> getLessonCommentByLessonId(Long lessonId);

    LessonCommentResponse updateLessonComment(UpdateLessonCommentRequest request);

    void deleteLessonComment(Long lessonCommentId);
}
