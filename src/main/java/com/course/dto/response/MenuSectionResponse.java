package com.course.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuSectionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String target;

    private int orderIndex = 1;

    private List<MenuLessonResponse> menuLessons = new ArrayList<>();

    private Long currentLessonId;
}
