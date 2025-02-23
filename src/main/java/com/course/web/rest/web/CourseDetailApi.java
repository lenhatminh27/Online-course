package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.CourseResponse;
import com.course.exceptions.NotFoundException;
import com.course.service.CourseService;
import com.course.service.impl.CourseServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/course/detail/*")
public class CourseDetailApi extends HttpServlet {

    private Gson gson;

    private CourseService courseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.gson = getBean(Gson.class.getSimpleName());
        this.courseService = getBean(CourseServiceImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo(); // "/1"
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id khóa học là bắt buộc."));
            return;
        }
        try {
            Long courseId = Long.parseLong(pathInfo.substring(1));
            CourseResponse course = courseService.findById(courseId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(course));
        }
        catch (NotFoundException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        }
        catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id khóa học là bắt buộc."));
        }
    }
}
