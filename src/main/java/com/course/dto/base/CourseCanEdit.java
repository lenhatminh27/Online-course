package com.course.dto.base;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CourseCanEdit implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean canEdit;

}
