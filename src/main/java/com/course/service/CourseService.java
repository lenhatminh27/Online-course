package com.course.service;

import com.course.dto.request.CreateCourseRequest;
import com.course.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getAllListCourseByUserCurrent();

    CourseResponse createCourse(CreateCourseRequest createCourseRequest);
}
