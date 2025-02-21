package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.LessonCommentDAO;
import com.course.dao.CourseLessonDAO;
import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.LessonCommentResponse;
import com.course.entity.AccountEntity;
import com.course.entity.LessonCommentEntity;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.LessonCommentService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonCommentServiceImpl implements LessonCommentService {

    private final LessonCommentDAO lessonCommentDAO;

    private final AccountDAO accountDAO;

    private final CourseLessonDAO lessonDAO;

    @Override
    public LessonCommentResponse createLessonComment(CreateLessonCommentRequest request) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);
        LessonCommentEntity parentComment = null;
        if(request.getParentId() != null) {
            parentComment = lessonCommentDAO.findLessonCommentById(request.getParentId());
        }
        LessonCommentEntity lessonCommentEntity = new LessonCommentEntity();
        lessonCommentEntity.setCourseLesson(lessonDAO.findCourseLessonById(request.getLessonId()));
        lessonCommentEntity.setAccount(accountEntity);
        lessonCommentEntity.setContent(request.getContent());
        lessonCommentEntity.setCreatedAt(LocalDateTime.now());
        lessonCommentEntity.setParentLessonComment(parentComment);
        LessonCommentEntity lessonCommentSave = lessonCommentDAO.createLessonComment(lessonCommentEntity);
        return convertToLessonCommentResponse(lessonCommentSave);
    }

    private LessonCommentResponse convertToLessonCommentResponse(LessonCommentEntity lessonCommentEntity) {
        AccountResponse accountResponse = convertToAccountResponse(lessonCommentEntity.getAccount());
        List<LessonCommentEntity> listChildren = lessonCommentDAO.findAllChildrenLessonComments(lessonCommentEntity.getId());
        List<LessonCommentResponse> lessonCommentResponseList = new ArrayList<>();
        if (listChildren != null) {
            for (LessonCommentEntity child : listChildren) {
                lessonCommentResponseList.add(convertToLessonCommentResponse(child));
            }
        }
        return new LessonCommentResponse(
                lessonCommentEntity.getId(),
                lessonCommentEntity.getCourseLesson().getId(),
                lessonCommentEntity.getContent(),
                accountResponse,
                lessonCommentEntity.getCreatedAt().toString(),
                lessonCommentEntity.getUpdatedAt() != null ? lessonCommentEntity.getUpdatedAt().toString() : null,
                lessonCommentResponseList
        );
    }

    private AccountResponse convertToAccountResponse(AccountEntity accountEntity) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail(accountEntity.getEmail());
        accountResponse.setAvatar(accountEntity.getAvatar());
        return accountResponse;
    }
}
