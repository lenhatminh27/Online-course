package com.course.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CreateLessonRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sectionId;

    private String title;

    private String description;

    private String videoUrl;

    private String article;

    private Long duration = 0L;

    private boolean isTrial = false;

    private int orderIndex = 1;

}
