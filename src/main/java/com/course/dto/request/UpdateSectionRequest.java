package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSectionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sectionId;

    private String title;

    private String target;

    private int orderIndex = 1;

}
