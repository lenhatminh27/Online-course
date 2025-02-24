package com.course.service.async.impl;

import com.course.core.bean.annotations.Service;
import com.course.core.scheduling.annotations.Async;
import com.course.service.async.FileSerivce;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.course.core.bean.BeanListener.BeanContext.getBean;
import static com.course.storage.MinioServiceImpl.BUCKET_NAME;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileSerivce {

    private final MinioClient minioClient;

    public FileServiceImpl() {
        this.minioClient = getBean(MinioClient.class.getSimpleName());
    }

    @Override
    @Async
    @SneakyThrows
    public void deleteFile(String url) {
        try {
            String objectName = extractObjectName(url);
            if (objectName == null || objectName.isEmpty()) {
                System.err.println("Lỗi: Object name không hợp lệ!");
                return;
            }
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build()
            );

            System.out.println("Video đã được xóa thành công: " + objectName);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa video: " + e.getMessage());
        }
    }

    private String extractObjectName(String url) {
        try {
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
            String cleanUrl = decodedUrl.split("\\?")[0];
            return cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            System.err.println("Lỗi khi trích xuất object name: " + e.getMessage());
            return null;
        }
    }
}
