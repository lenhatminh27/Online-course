package com.course.dto.response;

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
public class BookmarksBlogResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long blogId;
    private String title;
    private String createBy;
    private String slug;
    private String createAt;

}
