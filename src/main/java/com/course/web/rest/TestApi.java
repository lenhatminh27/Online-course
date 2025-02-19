package com.course.web.rest;

import com.course.common.utils.ResponseUtils;
import com.course.storage.MinioService;
import com.course.storage.MinioServiceImpl;
import com.course.storage.model.UploadFileArg;
import com.google.gson.Gson;
import io.minio.MinioClient;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/test")
@MultipartConfig
public class TestApi extends HttpServlet {

    private Gson gson;

    private MinioService minioService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        minioService = getBean(MinioServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        UploadFileArg arg = UploadFileArg.builder()
                .path("")
                .part(filePart)
                .build();
        String filePath = minioService.upload(MinioServiceImpl.BUCKET_NAME, arg);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(filePath));
    }
}
