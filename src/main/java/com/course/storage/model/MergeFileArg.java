package com.course.storage.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MergeFileArg {
    private String fileName;
}

