package com.course.dto.response;

import com.course.entity.enums.CourseStatus;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CourseListRespone implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private String thumbnail;

    private BigDecimal price = BigDecimal.ZERO;

    private CourseStatus status;

    private Double rating;

    private boolean isWishlist;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private AccountResponse accountResponse;

    private List<CategoryResponse> categories;
}
