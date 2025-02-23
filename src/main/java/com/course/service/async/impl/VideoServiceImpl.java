package com.course.service.async.impl;

import com.course.core.bean.annotations.Service;
import com.course.core.scheduling.annotations.Async;
import com.course.service.async.VideoService;
import com.course.storage.model.MergeFileArg;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.course.core.bean.BeanListener.BeanContext.getBean;
import static com.course.storage.MinioServiceImpl.BUCKET_NAME;

@Service
public class VideoServiceImpl implements VideoService {

    private final MinioClient minioClient;

    public VideoServiceImpl() {
        this.minioClient = getBean(MinioClient.class.getSimpleName());
    }

    @Override
    @Async
    @SneakyThrows
    public void remove(String url) {
        try {
            // Lấy đúng object name
            String objectName = extractObjectName(url);
            if (objectName == null || objectName.isEmpty()) {
                System.err.println("Lỗi: Object name không hợp lệ!");
                return;
            }
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object("videos/" + objectName)
                            .build()
            );

            System.out.println("Video đã được xóa thành công: " + objectName);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa video: " + e.getMessage());
        }
    }

    @Override
    @Async
    @SneakyThrows
    public void removeAfterMerge(int chunkIndex, File mergedFile, MergeFileArg arg, String mergedFilePath) {
        for (int i = 0; i < chunkIndex; i++) {
            String chunkObject = "chunks/" + arg.getFileName() + "/chunk_" + i;
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(chunkObject)
                    .build());
            System.out.println("🗑️ Đã xóa chunk: " + chunkObject);
        }
        // Xóa file tạm
        if (mergedFile.delete()) {
            System.out.println("🗑️ Xóa file tạm sau khi upload thành công: " + mergedFilePath);
        } else {
            System.err.println("⚠️ Không thể xóa file tạm: " + mergedFilePath);
        }
    }


    private String extractObjectName(String url) {
        try {
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);

            // Loại bỏ query parameters (chỉ lấy phần trước dấu '?')
            String cleanUrl = decodedUrl.split("\\?")[0];
            return cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            System.err.println("Lỗi khi trích xuất object name: " + e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        String url = "http://localhost:9000/resource/videos/resolve-confict-khi-pullrequest.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20250220%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250220T150614Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=89e239f79463705dba213072b1f7bac4177d1b4ca35950c05d3a918fe44c18de";
        String s = url.split("\\?")[0];
        System.out.println(s.substring(s.lastIndexOf("/") + 1));
    }
}
