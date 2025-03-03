package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRatingRequest {
    @Serial
    private static final long serialVersionUID = 1L;
    String review;
    Integer rating;
    Long courseId;
}
