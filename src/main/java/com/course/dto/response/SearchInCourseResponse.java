package com.course.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchInCourseResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<MenuSectionResponse> sections;

    private List<MenuLessonResponse> lessons;
}
