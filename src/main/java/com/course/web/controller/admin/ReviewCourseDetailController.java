package com.course.web.controller.admin;

import com.course.common.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/review-course-detail/*")
public class ReviewCourseDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (!ObjectUtils.isEmpty(pathInfo) && pathInfo.length() > 1) {
            req.getRequestDispatcher("/views/admin/review-course-detail.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/404");
        }
    }
}

