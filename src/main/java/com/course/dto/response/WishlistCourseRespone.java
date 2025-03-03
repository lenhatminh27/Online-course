package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistCourseRespone {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long courseId;
    private String title;
    private String createBy;
    private String description;
    private String createdAt;
    private BigDecimal price;
    private Double rating ;
    private String imageUrl;

}
