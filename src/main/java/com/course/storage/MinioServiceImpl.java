package com.course.storage;

import com.course.common.utils.ConverterUtils;
import com.course.core.bean.annotations.Service;
import com.course.storage.model.DownloadFileArg;
import com.course.storage.model.UploadFileArg;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;

import java.io.InputStream;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@Service
public final class MinioServiceImpl implements MinioService {

    public static final String BUCKET_NAME = "resource";

    private final MinioClient minioClient;

    public MinioServiceImpl() {
        this.minioClient = getBean(MinioClient.class.getSimpleName());
    }

    @Override
    @SneakyThrows
    public String upload(String bucket, UploadFileArg arg) {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(BUCKET_NAME)
                .build());
        if (!found){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());
        }
        Part part = arg.getPart();
        String fileName = part.getSubmittedFileName();
        String pathFile = String.join("/", arg.getPath(), fileName);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(pathFile)
                        .stream(part.getInputStream(), part.getInputStream().available(), -1)
                        .contentType(part.getContentType())
                        .build()
        );
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(pathFile)
                        .build()
        );
    }

    @Override
    public DownloadFileArg download(String bucket, String filename) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .build()
            );
            try (InputStream response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .build()
            )) {
                byte[] bytes = ConverterUtils.readBytesFromInputStream(response, (int) stat.size());
                return DownloadFileArg.builder()
                        .fileName(filename)
                        .bytes(bytes)
                        .contentType(stat.contentType())
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to download file: " + e.getMessage(), e);
        }
    }

}
