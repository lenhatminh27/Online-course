package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.*;
import com.course.dto.response.*;
import com.course.entity.*;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContext;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.MenuSectionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuSectionServiceImpl implements MenuSectionService {

    private final CourseDAO courseDAO;

    private final SectionDAO sectionDAO;

    private final LessonDAO lessonDAO;

    private final EnrollmentDAO enrollmentDAO;

    private final AccountDAO accountDAO;

    @Override
    public List<MenuSectionResponse> getMenuSectionByCourseId(Long courseId) {
        CourseEntity course = courseDAO.findById(courseId);
        if (course == null) {
            throw new ForbiddenException("Không tìm thấy course!");
        }
        AuthenticationContext context = AuthenticationContextHolder.getContext();
        AccountEntity account = accountDAO.findByEmail(context.getEmail());
        boolean enrollment = enrollmentDAO.getEnrollmentByAccountIdAndCourseId(account.getId(), courseId);
        if (course.getCreatedBy().equals(account.getEmail())) {
            enrollment = true;
        }
        if (context.getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN)) {
            enrollment = true;
        }
        if (!enrollment) {
            throw new ForbiddenException("Bạn chưa đăng ký khoá học này");
        }
        List<CourseSectionEntity> list = sectionDAO.findByCourse(course);
        return list.stream().map(this::convertToMenuSectionResponse).toList();
    }

    private MenuSectionResponse convertToMenuSectionResponse(CourseSectionEntity section) {
        List<CourseLessonEntity> listLesson = lessonDAO.findBySection(section);
        CourseEntity course = section.getCourse();
        return MenuSectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .target(section.getTarget())
                .orderIndex(section.getOrderIndex())
                .menuLessons(listLesson.stream().map(this::convertToMenuLessonResponse).toList())
                .build();
    }

    private CourseResponse convertToCourseResponse(CourseEntity course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())
                .build();
    }

    private MenuLessonResponse convertToMenuLessonResponse(CourseLessonEntity lesson){
        return MenuLessonResponse.builder()
                .id(lesson.getId())
                .sectionId(lesson.getCourseSection().getId())
                .title(lesson.getTitle())
                .duration(lesson.getDuration())
                .isTrial(lesson.isTrial())
                .orderIndex(lesson.getOrderIndex())
                .build();
    }
}
