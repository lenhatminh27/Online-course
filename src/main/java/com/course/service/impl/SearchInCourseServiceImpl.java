package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.CourseDAO;
import com.course.dao.LessonDAO;
import com.course.dao.SectionDAO;
import com.course.dto.request.SearchInCourseRequest;
import com.course.dto.response.*;
import com.course.entity.CourseEntity;
import com.course.entity.CourseLessonEntity;
import com.course.entity.CourseSectionEntity;
import com.course.service.SearchInCourseService;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchInCourseServiceImpl implements SearchInCourseService {

    private final SectionDAO sectionDAO;

    private final CourseDAO courseDAO;

    private final LessonDAO lessonDAO;

    @Override
    public SearchInCourseResponse searchInCourse(SearchInCourseRequest searchInCourseRequest) {
        CourseEntity course = courseDAO.findById(searchInCourseRequest.getCourseId());
        List<CourseSectionEntity> listSection = sectionDAO.searchSectionsInCourse(course.getId(), searchInCourseRequest.getContent());
        List<MenuSectionResponse> menuSectionResponses = new ArrayList<>();

        for (CourseSectionEntity section : listSection) {
            menuSectionResponses.add(convertToMenuSectionResponses(section));
        }
        List<CourseSectionEntity> sections = new ArrayList<>();
        if (!ObjectUtils.isEmpty(listSection)) {
            List<Long> listSectionId = listSection.stream().map(CourseSectionEntity::getId).collect(Collectors.toList());
            sections = sectionDAO.findByCourseNotIn(listSectionId, course);
        }
        else {
            sections = sectionDAO.findByCourse(course);
        }
        List<CourseLessonEntity> lessons = lessonDAO.searchLessonsInCourse(sections.stream().map(it -> it.getId()).toList(), searchInCourseRequest.getContent());
        List<MenuLessonResponse> menuLessonResponses = Collections.emptyList();
        if (!ObjectUtils.isEmpty(lessons)) {
            menuLessonResponses = lessons.stream().map(lesson -> convertToMenuLessonResponse(lesson)).toList();
        }
        return SearchInCourseResponse.builder()
                .sections(menuSectionResponses)
                .lessons(menuLessonResponses)
                .build();
    }

    private MenuSectionResponse convertToMenuSectionResponses(CourseSectionEntity section) {
        List<CourseLessonEntity> listLesson = lessonDAO.findBySection(section);
        return MenuSectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .target(null)
                .orderIndex(section.getOrderIndex())
                .menuLessons(listLesson.stream().map(this::convertToMenuLessonResponse).toList())
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
