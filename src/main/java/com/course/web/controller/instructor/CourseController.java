package com.course.web.controller.instructor;

import com.course.common.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/instructor/course/*")
public class CourseController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(!ObjectUtils.isEmpty(pathInfo)) {
            if(pathInfo.contains("manage/curriculum")){
                req.getRequestDispatcher("/views/instructor/curriculum-course.jsp").forward(req, resp);
                return;
            }
            if(pathInfo.contains("manage/basic")){
                req.getRequestDispatcher("/views/instructor/manage-basic-course.jsp").forward(req, resp);
                return;
            }
        }
        req.getRequestDispatcher("/views/instructor/views-course.jsp").forward(req, resp);
    }

}
