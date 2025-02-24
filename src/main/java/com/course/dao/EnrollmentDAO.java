package com.course.dao;

import com.course.entity.EnrollmentEntity;

import java.util.List;

public interface EnrollmentDAO {
    boolean getEnrollmentByAccountIdAndCourseId(Long accountId, Long courseId);
}
