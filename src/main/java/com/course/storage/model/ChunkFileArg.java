package com.course.storage.model;

import lombok.*;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChunkFileArg {
    private String path;
    InputStream fileStream;
    long fileSize;
}
