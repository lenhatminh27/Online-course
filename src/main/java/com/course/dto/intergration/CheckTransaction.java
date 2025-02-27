package com.course.dto.intergration;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CheckTransaction implements Serializable {
    private boolean success;
}
