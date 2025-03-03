package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class RatingUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String review;
    private Integer rating;
}