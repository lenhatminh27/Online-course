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
public class CourseFilterRequest extends FilterRequest {

    private Sort sort;

    public CourseFilterRequest(Sort sort, String search, int page, int size) {
        super(search, page, size);
        this.sort = sort;
    }
}
