package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private List<String> tagName = new ArrayList<>();

}
