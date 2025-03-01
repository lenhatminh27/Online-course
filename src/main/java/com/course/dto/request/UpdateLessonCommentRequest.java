package com.course.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UpdateLessonCommentRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long commentId;

    private String content;
}
