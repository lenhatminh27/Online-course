package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponse implements Serializable {

    private Long id;

    private String title;

    private String target;

    private int orderIndex = 1;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<LessonResponse> lessons = new ArrayList<>();
}
