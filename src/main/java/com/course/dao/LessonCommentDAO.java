package com.course.dao;

import com.course.entity.BlogCommentEntity;
import com.course.entity.LessonCommentEntity;

import java.util.List;

public interface LessonCommentDAO {

    LessonCommentEntity createLessonComment(LessonCommentEntity lessonComment);

    List<LessonCommentEntity> findAllChildrenLessonComments(Long id);

    LessonCommentEntity findLessonCommentById(Long id);

    void deleteLessonCommentByLessonId(Long lessonId);
}
