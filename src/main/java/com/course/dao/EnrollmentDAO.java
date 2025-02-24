package com.course.dao;

public interface EnrollmentDAO {
    boolean getEnrollmentByAccountIdAndCourseId(Long accountId, Long courseId);
}
