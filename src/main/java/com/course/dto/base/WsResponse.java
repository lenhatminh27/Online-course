package com.course.dto.base;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    int status;

    String message;

}
