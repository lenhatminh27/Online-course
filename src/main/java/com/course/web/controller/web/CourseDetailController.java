package com.course.web.controller.web;

import com.course.common.utils.ObjectUtils;
import com.course.security.annotations.handle.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/course/*")

public class CourseDetailController extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (!ObjectUtils.isEmpty(pathInfo) && pathInfo.length() > 1) {
            req.getRequestDispatcher("/views/web/view-course-detail.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/404");
        }
    }
}
