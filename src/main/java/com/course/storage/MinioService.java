package com.course.storage;

import com.course.storage.model.ChunkFileArg;
import com.course.storage.model.DownloadFileArg;
import com.course.storage.model.MergeFileArg;
import com.course.storage.model.UploadFileArg;

public sealed interface MinioService permits MinioServiceImpl{

    String upload(String bucket, UploadFileArg uploadFileArg);

    DownloadFileArg download(String bucket, String filename);

    void uploadChunk(String bucket, ChunkFileArg arg);

    String mergeTruck(MergeFileArg arg);
}
