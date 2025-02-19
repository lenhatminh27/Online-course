package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private Long categoriesId;

}
