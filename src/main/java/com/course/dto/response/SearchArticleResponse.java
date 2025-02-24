package com.course.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchArticleResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private MenuLessonResponse menuLesson;

    private String article;
}
