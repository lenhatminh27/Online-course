package com.course.exceptions;

import com.course.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorResponse error;

    public BadRequestException(ErrorResponse error) {
        this.error = error;
    }

}
