package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.CourseDAO;
import com.course.dao.LessonDAO;
import com.course.dao.SectionDAO;
import com.course.dto.request.CreateSectionRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonResponse;
import com.course.dto.response.SectionResponse;
import com.course.entity.CourseEntity;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.NotFoundException;
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

    @Override
    public SectionResponse creatSection(CreateSectionRequest section) {
        CourseEntity course = courseDAO.findById(section.getCourseId());
        if (ObjectUtils.isEmpty(course)) {
            throw new NotFoundException("Không tìm thấy course id tương ứng");
        }
        if (sectionDAO.existTitle(section.getTitle())){
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
        List<CourseSectionEntity> list = sectionDAO.findByCourse(course);
        return list.stream().map(this::convertToSectionResponse).toList();
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
