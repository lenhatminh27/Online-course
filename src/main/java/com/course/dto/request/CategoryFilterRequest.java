package com.course.dto.request;

import com.course.core.repository.data.Sort;
import com.course.dto.base.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFilterRequest extends FilterRequest {
    private Sort sort;


    public CategoryFilterRequest(Sort sort,String search, int page) {
        super(search, page);
        this.sort = sort;
    }
}
