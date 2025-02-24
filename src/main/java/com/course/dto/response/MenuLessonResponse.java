package com.course.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuLessonResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long sectionId;

    private String title;

    private String searchArticle;

    private Long duration = 0L;

    private boolean isTrial = false;

    private int orderIndex = 1;

    private boolean done = false;
}
