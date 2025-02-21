package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.FileResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.storage.MinioService;
import com.course.storage.MinioServiceImpl;
import com.course.storage.model.ChunkFileArg;
import com.course.storage.model.MergeFileArg;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/truck")
@MultipartConfig
public class UploadTruckApi extends BaseServlet {

    private Gson gson;

    private MinioService minioService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        minioService = getBean(MinioServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("UPLOAD_FILE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String fileName = req.getParameter("fileName");
            int chunkIndex = Integer.parseInt(req.getParameter("chunkIndex"));
            InputStream fileStream = req.getPart("file").getInputStream();
            String objectName = "chunks/" + fileName + "/chunk_" + chunkIndex;
            ChunkFileArg arg = ChunkFileArg.builder()
                    .path(objectName)
                    .fileStream(fileStream)
                    .fileSize(fileStream.available())
                    .build();
            minioService.uploadChunk(MinioServiceImpl.BUCKET_NAME, arg);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Upload success"));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Internal Error"));
        }
    }


    @Override
    @IsAuthenticated
    @HasPermission("UPLOAD_FILE")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            BufferedReader reader = req.getReader();
            MergeFileArg arg = gson.fromJson(reader, MergeFileArg.class);
            if (arg.getFileName() == null || arg.getFileName().isEmpty()) {
                System.err.println("🚨 Thiếu `fileName` trong request body!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String videoUrl = minioService.mergeTruck(arg);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(new FileResponse(videoUrl)));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi ghép file!");
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson(Map.of("success", false, "message", "Lỗi hệ thống")));
        }
    }

}
