package com.course.dto.response;

import com.course.entity.BlogCommentEntity;
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
public class BlogCommentResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long blogId;

    private AccountResponse accountResponse;

    private String content;

    private List<BlogCommentResponse> childrenComments;

    private String createdAt;

    private String updatedAt;

}
