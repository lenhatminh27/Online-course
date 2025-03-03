package com.course.dto.response;

import com.course.entity.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRegistedRespone implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private String thumbnail;

    private BigDecimal price = BigDecimal.ZERO;

    private CourseStatus status;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double rating;

    private AccountResponse accountResponse;

    private List<CategoryResponse> categories;
}
