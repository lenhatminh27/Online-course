package com.course.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class UpdateLessonRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long lessonId;

    private String title;

    private String description;

    private String videoUrl;

    private String article;

    private Long duration;

    private boolean isTrial;

    private int orderIndex;

}
