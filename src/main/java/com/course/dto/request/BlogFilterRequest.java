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
public class BlogFilterRequest extends FilterRequest {


    private List<String> tags = new ArrayList<>();

    private Sort sort;

    private Long idOwner;

    public BlogFilterRequest(List<String> tags, Sort sort,String search, int page, Long idOwner) {
        super(search, page);
        this.tags = tags;
        this.sort = sort;
        this.idOwner = idOwner;
    }

}
