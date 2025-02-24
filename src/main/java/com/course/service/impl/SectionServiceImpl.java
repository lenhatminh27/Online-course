package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.*;
import com.course.dto.request.CreateSectionRequest;
import com.course.dto.request.UpdateSectionRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonResponse;
import com.course.dto.response.SectionResponse;
import com.course.entity.AccountEntity;
import com.course.entity.CourseEntity;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.SectionService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionDAO sectionDAO;

    private final CourseDAO courseDAO;

    private final LessonDAO lessonDAO;

    private final EnrollmentDAO enrollmentDAO;

    private final AccountDAO accountDAO;

    @Override
    public SectionResponse creatSection(CreateSectionRequest section) {
        CourseEntity course = courseDAO.findById(section.getCourseId());
        if (ObjectUtils.isEmpty(course)) {
            throw new NotFoundException("Không tìm thấy course id tương ứng");
        }
        if (sectionDAO.existTitle(section.getTitle(), course.getId())){
            List<String> error = new ArrayList<>();
            error.add("Tiêu đề đã tồn tại");
            throw new BadRequestException(new ErrorResponse(error));
        }
        int order = sectionDAO.countSectionsByCourse(course) + 1;
        CourseSectionEntity newSection = CourseSectionEntity.builder()
                .title(section.getTitle())
                .target(section.getTarget())
                .course(course)
                .orderIndex(order)
                .build();
        CourseSectionEntity sectionRes = sectionDAO.createSection(newSection);
        return convertToSectionResponse(sectionRes);
    }

    @Override
    public List<SectionResponse> getSectionByCourseId(Long courseId) {
        CourseEntity course = courseDAO.findById(courseId);
        if (ObjectUtils.isEmpty(course)) {
            throw new NotFoundException("Không tìm thấy course id tương ứng");
        }
        AccountEntity accountCurrent = getAuthenticatedAccountCurrent();
        boolean isEnrolled = enrollmentDAO.getEnrollmentByAccountIdAndCourseId(accountCurrent.getId(), courseId);
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        assert accountCurrent != null;
        boolean isOwner = course.getAccountCreated().getEmail().equals(accountCurrent.getEmail());
        if (!isAdmin && !isOwner && !isEnrolled) {
            throw new ForbiddenException("Không có quyền xem");
        }
        List<CourseSectionEntity> list = sectionDAO.findByCourse(course);
        return list.stream().map(this::convertToSectionResponse).toList();
    }



    @Override
    public SectionResponse updateSection(UpdateSectionRequest updateSection) {
        CourseSectionEntity sectionUpdated = sectionDAO.findById(updateSection.getSectionId());
        if (ObjectUtils.isEmpty(sectionUpdated)) {
            throw new NotFoundException("Không tìm thấy phần học id tương ứng");
        }
        String accountCurrent = getAuthenticatedAccount();
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        boolean isOwner = sectionUpdated.getCourse().getAccountCreated().getEmail().equals(accountCurrent);
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("Không có quyền thay đổi");
        }
        if(!ObjectUtils.isEmpty(updateSection.getTitle())){
            if(!sectionUpdated.getTitle().equals(updateSection.getTitle())){
                if(sectionDAO.existTitle(updateSection.getTitle(), sectionUpdated.getId())){
                    List<String> error = new ArrayList<>();
                    error.add("Tiêu đề đã tồn tại");
                    throw new BadRequestException(new ErrorResponse(error));
                }
                else{
                    sectionUpdated.setTitle(updateSection.getTitle());
                }
            }
        }
        sectionUpdated.setTarget(updateSection.getTarget());
        if(!ObjectUtils.isEmpty(updateSection.getOrderIndex())){
            sectionUpdated.setOrderIndex(updateSection.getOrderIndex());
        }
        CourseSectionEntity courseUpdate = sectionDAO.updateSection(sectionUpdated);
        return convertToSectionResponse(courseUpdate);
    }


    private String getAuthenticatedAccount() {
        return AuthenticationContextHolder.getContext().getEmail();
    }

    private AccountEntity getAuthenticatedAccountCurrent() {
        if (AuthenticationContextHolder.getContext() == null) {
            return null;
        }
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }

    private SectionResponse convertToSectionResponse(CourseSectionEntity section) {
        List<CourseLessonEntity> listLesson = lessonDAO.findBySection(section);
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .target(section.getTarget())
                .orderIndex(section.getOrderIndex())
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .lessons(listLesson.stream().map(this::convertToLessonResponse).toList())
                .build();
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
}
