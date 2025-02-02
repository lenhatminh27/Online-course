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
public class BlogResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String slug;

    private String content;

    private AccountResponse accountResponse;

    private List<TagResponse> tagResponses;

    private String createAt;

}

