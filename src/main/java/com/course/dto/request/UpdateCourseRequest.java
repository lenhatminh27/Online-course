package com.course.dto.request;

import com.course.entity.enums.CourseStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpdateCourseRequest implements Serializable {

    private Long courseId;

    private String title;

    private String description;

    private String thumbnail;

    private BigDecimal price = BigDecimal.ZERO;

    private CourseStatus status;

    private Long categoriesId;
}
