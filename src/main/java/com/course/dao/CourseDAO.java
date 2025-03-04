package com.course.dao;

import com.course.dto.request.CourseFilterRequest;
import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.request.ReviewCourseFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CourseEntity;
import com.course.entity.enums.CourseStatus;

import java.util.List;

public interface CourseDAO {
    CourseEntity createCourse(CourseEntity course);

    CourseEntity findById(Long id);

    PageResponse<CourseEntity> findByAccountCreatedId(Long id, CourseInstructorFilterRequest filterRequest);

    CourseEntity updateCourse(CourseEntity course);

    void deleteCourse(Long id);

    void updateCourseStatus(Long id, CourseStatus status);

    List<CourseEntity> findByStatus(CourseStatus status);

    PageResponse<CourseEntity> getInReviewCourses(ReviewCourseFilterRequest filterRequest);

    List<CourseEntity> getTop3Courses();

    PageResponse<CourseEntity> getAllCourses(CourseFilterRequest filterRequest);
}
