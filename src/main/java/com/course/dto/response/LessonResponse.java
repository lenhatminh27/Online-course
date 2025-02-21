package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long sectionId;

    private String title;

    private String description;

    private String videoUrl;

    private String article;

    private Long duration = 0L;

    private boolean isTrial = false;

    private int orderIndex = 1;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
