package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int page;

    private int totalPages;

    private List<T> data = new ArrayList<T>();

}
