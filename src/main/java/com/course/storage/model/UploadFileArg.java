package com.course.storage.model;

import jakarta.servlet.http.Part;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileArg {
    private String path;
    private Part part;
}
