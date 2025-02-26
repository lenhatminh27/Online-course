package com.course.dto.request;

import com.course.core.repository.data.Sort;
import com.course.dto.base.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCourseFilterRequest extends FilterRequest {
    private Sort sort;
    
    public ReviewCourseFilterRequest(String search, int page, Sort sort) {
        super(search, page);
        this.sort = sort;
    }
}
