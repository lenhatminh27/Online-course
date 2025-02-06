package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
}
