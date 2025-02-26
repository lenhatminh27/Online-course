package com.course.service;

import com.course.dto.request.*;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;

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

}
