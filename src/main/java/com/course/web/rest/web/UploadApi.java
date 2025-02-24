package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.FileResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.storage.MinioService;
import com.course.storage.MinioServiceImpl;
import com.course.storage.model.UploadFileArg;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/upload")
@MultipartConfig
public class UploadApi extends BaseServlet {

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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Part filePart = req.getPart("file");
        final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
        if (filePart.getSize() > MAX_FILE_SIZE) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    gson.toJson("Kích thước tệp vượt quá giới hạn tối đa 10MB"));
            return;
        }
        UploadFileArg arg = UploadFileArg.builder()
                .path("")
                .part(filePart)
                .build();
        String filePath = minioService.upload(MinioServiceImpl.BUCKET_NAME, arg);
        FileResponse fileResponse = new FileResponse(filePath);
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(fileResponse));
    }
}
