package com.course.storage;

import com.course.common.utils.ConverterUtils;
import com.course.core.bean.annotations.Service;
import com.course.service.async.VideoService;
import com.course.service.async.impl.VideoServiceImpl;
import com.course.storage.model.ChunkFileArg;
import com.course.storage.model.DownloadFileArg;
import com.course.storage.model.MergeFileArg;
import com.course.storage.model.UploadFileArg;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;

import java.io.*;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@Service
public final class MinioServiceImpl implements MinioService {

    public static final String BUCKET_NAME = "resource";

    private final MinioClient minioClient;

    private final VideoService videoService;

    public MinioServiceImpl() {
        this.minioClient = getBean(MinioClient.class.getSimpleName());
        this.videoService = getBean(VideoServiceImpl.class.getSimpleName());
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

    @Override
    @SneakyThrows
    public void uploadChunk(String bucket, ChunkFileArg arg) {
        String pathFile = String.join("/", arg.getPath());
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(pathFile)
                .stream(arg.getFileStream(), arg.getFileSize(), -1)
                .build());
    }

    @SneakyThrows
    public String mergeTruck(MergeFileArg arg) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String mergedFilePath = tmpDir + File.separator + arg.getFileName();
        File mergedFile = new File(mergedFilePath);
        System.out.println("📂 Đường dẫn file merge: " + mergedFilePath);

        // Tạo thư mục tạm nếu chưa tồn tại
        File parentDir = mergedFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            System.out.println("📁 Tạo thư mục tạm: " + created);
        }

        int chunkIndex = 0;
        boolean chunkExists = true;

        try (FileOutputStream fos = new FileOutputStream(mergedFile, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            while (chunkExists) {
                String objectName = "chunks/" + arg.getFileName() + "/chunk_" + chunkIndex;
                try {
                    // Kiểm tra xem chunk có tồn tại không
                    minioClient.statObject(StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build());

                    try (InputStream chunkStream = minioClient.getObject(GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build())) {

                        System.out.println("🔄 Đang ghép chunk: " + objectName);
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = chunkStream.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                    chunkIndex++;
                } catch (Exception e) {
                    // Khi chunk không tồn tại nữa, kết thúc vòng lặp
                    chunkExists = false;
                    System.out.println("✅ Không còn chunk nào, hoàn tất ghép file!");
                }
            }
        }
        // Upload file hợp nhất lên MinIO
        String finalPath = "videos/" + arg.getFileName();
        try (FileInputStream fis = new FileInputStream(mergedFile)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(finalPath)
                            .stream(fis, mergedFile.length(), -1)
                            .build()
            );
            System.out.println("✅ Tải file hợp nhất lên MinIO thành công: " + finalPath);
        }
        videoService.removeAfterMerge(chunkIndex, mergedFile, arg, mergedFilePath);
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(BUCKET_NAME)
                        .object(finalPath)
                        .build()
        );
    }

}
