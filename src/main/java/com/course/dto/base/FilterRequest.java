package com.course.dto.base;

import com.course.common.constants.PageConstant;
import com.course.common.utils.ObjectUtils;
import com.course.core.repository.data.PageRequest;
import com.course.core.repository.data.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class FilterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected String search;

    protected Integer page = PageConstant.PAGE_CURRENT;

    protected Integer size = PageConstant.PAGE_LIMIT;

    protected FilterRequest(String search, Integer page) {
        this.search = search;
        this.page = page;
    }

    public PageRequest getPageRequest() {
        if(ObjectUtils.isEmpty(page)) {
            page = PageConstant.PAGE_CURRENT;
        }
        return PageRequest.of(page - 1, size);
    }

    public PageRequest getPageRequest(Sort sort) {
        if(ObjectUtils.isEmpty(page)) {
            page = PageConstant.PAGE_CURRENT;
        }
        return PageRequest.of(page - 1, size, sort);
    }

}
