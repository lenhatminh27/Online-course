package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.*;
import com.course.dto.request.CreateLessonRequest;
import com.course.dto.request.UpdateLessonRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonResponse;
import com.course.entity.AccountEntity;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.LessonCommentService;
import com.course.service.LessonService;
import com.course.service.async.VideoService;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonDAO lessonDAO;

    private final SectionDAO sectionDAO;

    private final VideoService videoService;

    private final LessonCommentDAO lessonCommentDAO;

    private final ProgressDAO progressDAO;

    @Override
    public LessonResponse createLesson(CreateLessonRequest createLessonRequest) {
        CourseSectionEntity section = sectionDAO.findById(createLessonRequest.getSectionId());
        if (section == null) {
            throw new NotFoundException("Không tìm thấy section id tương ứng");
        }
        if (lessonDAO.existsByTitle(createLessonRequest.getTitle(), section.getId())){
            List<String> error = new ArrayList<>();
            error.add("Tiêu đề đã tồn tại");
            throw new BadRequestException(new ErrorResponse(error));
        }
        int order = lessonDAO.countExistSection(section) + 1;
        CourseLessonEntity courseLesson = CourseLessonEntity.builder()
                .title(createLessonRequest.getTitle())
                .description(createLessonRequest.getDescription())
                .article(createLessonRequest.getArticle())
                .orderIndex(order)
                .courseSection(section)
                .videoUrl(createLessonRequest.getVideoUrl())
                .duration(0L)
                .isTrial(false)
                .build();
        CourseLessonEntity lesson = lessonDAO.createLesson(courseLesson);
        return convertToLessonResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(UpdateLessonRequest updateLessonRequest) {
        CourseLessonEntity lesson = lessonDAO.findById(updateLessonRequest.getLessonId());
        if(ObjectUtils.isEmpty(lesson)){
            throw new NotFoundException("Id bài học không tồn tại");
        }
        String accountCurrent = getAuthenticatedAccount();
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        boolean isOwner = lesson.getCourseSection().getCourse().getAccountCreated().getEmail().equals(accountCurrent);
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("Không có quyền thay đổi");
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getTitle())){
            if(!lesson.getTitle().equals(updateLessonRequest.getTitle())){
                if(lessonDAO.existsByTitle(updateLessonRequest.getTitle(), lesson.getCourseSection().getId())){
                    List<String> error = new ArrayList<>();
                    error.add("Tiêu đề đã tồn tại");
                    throw new BadRequestException(new ErrorResponse(error));
                }
                else{
                    lesson.setTitle(updateLessonRequest.getTitle());
                }
            }
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getDescription())){
            lesson.setDescription(updateLessonRequest.getDescription());
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getArticle())){
            if(updateLessonRequest.getArticle().equals("delete")){
                lesson.setArticle(null);
            }
            else{
                lesson.setArticle(updateLessonRequest.getArticle());
            }
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getVideoUrl())){
            lesson.setVideoUrl(updateLessonRequest.getVideoUrl());
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getDuration())){
            lesson.setDuration(updateLessonRequest.getDuration());
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.getOrderIndex())){
            lesson.setOrderIndex(updateLessonRequest.getOrderIndex());
        }
        if(!ObjectUtils.isEmpty(updateLessonRequest.isTrial())){
            lesson.setTrial(updateLessonRequest.isTrial());
        }
        CourseLessonEntity courseLesson = lessonDAO.updateLesson(lesson);
        return convertToLessonResponse(courseLesson);
    }

    @Override
    public void deleteVideo(Long lessonId) {
        CourseLessonEntity lesson = lessonDAO.findById(lessonId);
        if(ObjectUtils.isEmpty(lesson)){
            throw new NotFoundException("Id bài học không tồn tại");
        }
        String accountCurrent = getAuthenticatedAccount();
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        boolean isOwner = lesson.getCourseSection().getCourse().getAccountCreated().getEmail().equals(accountCurrent);
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("Không có quyền thay đổi");
        }
        videoService.remove(lesson.getVideoUrl());
        lesson.setVideoUrl(null);
        lessonDAO.updateLesson(lesson);
    }

    private String getAuthenticatedAccount() {
        return AuthenticationContextHolder.getContext().getEmail();
    }

    private LessonResponse convertToLessonResponse(CourseLessonEntity lesson){
        return LessonResponse.builder()
                .id(lesson.getId())
                .sectionId(lesson.getCourseSection().getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .article(lesson.getArticle())
                .orderIndex(lesson.getOrderIndex())
                .videoUrl(lesson.getVideoUrl())
                .duration(lesson.getDuration())
                .isTrial(lesson.isTrial())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .build();
    }

    @Override
    public LessonResponse getLessonById(Long lessonId) {
        CourseLessonEntity lesson = lessonDAO.findById(lessonId);
        if (ObjectUtils.isEmpty(lesson)) {
            throw new NotFoundException("Bài học không tồn tại!");
        }
        return convertToLessonResponse(lesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        CourseLessonEntity lesson = lessonDAO.findById(lessonId);
        if (lesson == null) {
            throw new NotFoundException("Bài học không tồn tại!");
        }

        String accountCurrent = getAuthenticatedAccount();
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        boolean isOwner = lesson.getCourseSection().getCourse().getAccountCreated().getEmail().equals(accountCurrent);
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("Bạn không có quyền xoá lesson này");
        }
        videoService.remove(lesson.getVideoUrl());
        lessonCommentDAO.deleteLessonCommentByLessonId(lessonId);
        progressDAO.deleteProgressByLessonId(lessonId);
        lessonDAO.deleteLesson(lessonId);
    }
}
