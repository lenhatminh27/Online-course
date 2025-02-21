package com.course.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CreateSectionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long courseId;

    private String title;

    private String target;

    private int orderIndex = 1;

}
