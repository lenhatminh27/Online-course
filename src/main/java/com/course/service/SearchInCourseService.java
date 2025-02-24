package com.course.service;

import com.course.dto.request.SearchInCourseRequest;
import com.course.dto.response.SearchInCourseResponse;

import java.util.List;

public interface SearchInCourseService {
    SearchInCourseResponse searchInCourse(SearchInCourseRequest searchInCourseRequest);
}
