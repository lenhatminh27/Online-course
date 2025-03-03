package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.LessonResponse;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.IsAuthenticated;
import com.course.service.LessonService;
import com.course.service.impl.LessonServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/public-lesson/*")

public class PublicLessonApi extends HttpServlet {
    private LessonService lessonService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        lessonService = getBean(LessonServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String courseIdStr = pathInfo.substring(1);
            try {
                Long courseId = Long.parseLong(courseIdStr);
                handleGetLessonById(courseId, resp);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid number"));
            }
            return;
        }
    }

    private void handleGetLessonById(Long courseId, HttpServletResponse resp) throws IOException {
        try {
            LessonResponse response = lessonService.getLessonById(courseId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        }
    }
}
