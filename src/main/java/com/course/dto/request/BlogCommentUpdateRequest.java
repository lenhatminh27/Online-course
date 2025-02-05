package com.course.dto.request;

import java.io.Serial;
import java.io.Serializable;

public class BlogCommentUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String content;
}
