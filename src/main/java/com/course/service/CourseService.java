package com.course.service;

import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.request.CreateCourseRequest;
import com.course.dto.request.UpdateCourseRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;

import java.util.List;

public interface CourseService {

    PageResponse<CourseResponse> getAllListCourseByUserCurrent(CourseInstructorFilterRequest filterRequest);

    CourseResponse updateCourse(UpdateCourseRequest request);

    CourseResponse createCourse(CreateCourseRequest createCourseRequest);

    CourseResponse findById(Long id);

}
