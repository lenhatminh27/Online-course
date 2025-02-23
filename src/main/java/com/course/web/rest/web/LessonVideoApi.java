package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.LessonService;
import com.course.service.impl.LessonServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/lesson/video/*")
public class LessonVideoApi extends BaseServlet {

    private LessonService lessonService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.lessonService = getBean(LessonServiceImpl.class.getSimpleName());
        this.gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("DELETE_COURSE")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy ID từ URL
        String pathInfo = req.getPathInfo(); // Lấy phần sau /api/lesson/video/
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Missing video ID in URL"));
            return;
        }
        String lessonId = pathInfo.substring(1);
        try {
            Long videoId = Long.parseLong(lessonId);
            lessonService.deleteVideo(videoId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("Video deleted"));
        }catch (NotFoundException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (ForbiddenException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        }
        catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid video ID");
        }
    }
}

