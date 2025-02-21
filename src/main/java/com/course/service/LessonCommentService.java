package com.course.service;

import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.response.LessonCommentResponse;

public interface LessonCommentService {

    LessonCommentResponse createLessonComment(CreateLessonCommentRequest request);
}
