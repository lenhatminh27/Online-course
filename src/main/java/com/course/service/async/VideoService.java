package com.course.service.async;

import com.course.storage.model.MergeFileArg;

import java.io.File;

public interface VideoService {
    void remove(String url);

    void removeAfterMerge(int chunkIndex,  File mergedFile, MergeFileArg arg, String mergedFilePath);
}
