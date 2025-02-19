package com.course.service;

import com.course.dto.request.CreateCourseRequest;
import com.course.dto.response.CourseResponse;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest createCourseRequest);
}
