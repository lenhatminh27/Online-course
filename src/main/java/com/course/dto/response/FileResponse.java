package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class FileResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String file;
}
