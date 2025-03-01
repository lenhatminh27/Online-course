package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.LessonCommentDAO;
import com.course.dao.LessonDAO;
import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonCommentResponse;
import com.course.entity.AccountEntity;
import com.course.entity.LessonCommentEntity;
import com.course.exceptions.BadRequestException;
import com.course.security.AuthoritiesConstants;
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

    private final LessonDAO lessonDAO;

    @Override
    public LessonCommentResponse createLessonComment(CreateLessonCommentRequest request) {
        if (!AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_INSTRUCTOR) && request.getContent().length() > 500) {
            List<String> error = new ArrayList<>();
            error.add("Bình luận của bạn không được vượt quá 500 ký tự!");
            throw new BadRequestException(new ErrorResponse(error));
        }
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);
        LessonCommentEntity parentComment = null;
        if(request.getParentId() != null) {
            parentComment = lessonCommentDAO.findLessonCommentById(request.getParentId());
        }
        LessonCommentEntity lessonCommentEntity = new LessonCommentEntity();
        lessonCommentEntity.setCourseLesson(lessonDAO.findById(request.getLessonId()));
        lessonCommentEntity.setAccount(accountEntity);
        lessonCommentEntity.setContent(request.getContent());
        lessonCommentEntity.setCreatedAt(LocalDateTime.now());
        lessonCommentEntity.setParentLessonComment(parentComment);
        LessonCommentEntity lessonCommentSave = lessonCommentDAO.createLessonComment(lessonCommentEntity);
        return convertToLessonCommentResponse(lessonCommentSave);
    }

    @Override
    public List<LessonCommentResponse> getLessonCommentByLessonId(Long lessonId) {
        List<LessonCommentEntity> noParentLessonComments = lessonCommentDAO.findNoParentLessonCommentsByLessonId(lessonId);
        List<LessonCommentResponse> lessonCommentResponses = new ArrayList<>();
        if (!ObjectUtils.isEmpty(noParentLessonComments)) {
            for (LessonCommentEntity lessonCommentEntity : noParentLessonComments) {
                lessonCommentResponses.add(convertToLessonCommentResponse(lessonCommentEntity));
            }
        }
        return lessonCommentResponses;
    }


    private LessonCommentResponse convertToLessonCommentResponse(LessonCommentEntity lessonCommentEntity) {
        AccountResponse accountResponse = convertToAccountResponse(lessonCommentEntity.getAccount());
        List<LessonCommentEntity> listChildren = lessonCommentDAO.findAllChildrenLessonComments(lessonCommentEntity.getId());
        List<LessonCommentResponse> lessonCommentResponseList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(listChildren)) {
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
