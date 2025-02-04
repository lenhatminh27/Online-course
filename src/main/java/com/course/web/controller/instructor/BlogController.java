package com.course.web.controller.instructor;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/instructor/blogs/*")
public class BlogController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null || action.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required");
            return;
        }
        switch (action) {
            case "create":
                req.getRequestDispatcher("/views/instructor/create-blog.jsp").forward(req, resp);
                break;
            case "view":
                req.getRequestDispatcher("/views/instructor/view-blogs.jsp").forward(req, resp);
                break;
            case "update":
                req.getRequestDispatcher("/views/instructor/update-blog.jsp").forward(req, resp);
                break;
            case "delete":
                req.getRequestDispatcher("/views/instructor/delete-blog.jsp").forward(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }
}
