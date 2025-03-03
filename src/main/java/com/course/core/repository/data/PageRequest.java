package com.course.core.repository.data;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Sort sort;
    private final int pageNumber;
    private final int pageSize;

    protected PageRequest(int pageNumber, int pageSize, Sort sort) {
        this.sort = sort;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static PageRequest of(int pageNumber, int pageSize) {
        return of(pageNumber, pageSize, Sort.unsorted());
    }

    public static PageRequest of(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber, pageSize, sort);
    }

}
