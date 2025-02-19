package com.course.storage.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadFileArg {
    private String fileName;
    private String contentType;
    private byte[] bytes;
}
