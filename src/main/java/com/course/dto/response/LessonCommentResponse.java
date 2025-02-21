package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCommentResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long lessonId;

    private String content;

    private AccountResponse account;

    private String createdAt;

    private String updatedAt;

    private List<LessonCommentResponse> childrenComments;
}
