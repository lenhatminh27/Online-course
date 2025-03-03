package com.course.service;

import com.course.dto.request.*;
import com.course.dto.response.CourseListRespone;
import com.course.dto.response.CourseRegistedRespone;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;
import com.course.entity.CourseEntity;
import com.course.entity.enums.CourseStatus;

import java.util.List;

public interface CourseService {

    PageResponse<CourseResponse> getAllListCourseByUserCurrent(CourseInstructorFilterRequest filterRequest);

    CourseResponse updateCourse(UpdateCourseRequest request);

    CourseResponse createCourse(CreateCourseRequest createCourseRequest);

    CourseResponse findById(Long id);

    void acceptCourse(Long courseId);

    void rejectCourse(Long courseId);

    PageResponse<CourseResponse> getInReviewCourse(ReviewCourseFilterRequest filterRequest);

    void sendReviewCourseDetailEmail(ReviewCourseDetailRequest reviewCourseDetailRequest);

    PageResponse<CourseListRespone> getAllCoursePublic(CourseFilterRequest filterRequest);

    List<CourseListRespone> getTop3Course();

    CourseListRespone convertToCourseListRespone(CourseEntity courseEntity);

    boolean checkCourseCanEdit(Long courseId);

    List<CourseRegistedRespone> getRegisteredCourse();

    CourseRegistedRespone convertToCourseRegistedResponse(CourseEntity courseEntity);

    void updateStatus(Long courseId, CourseStatus courseStatus);
}
