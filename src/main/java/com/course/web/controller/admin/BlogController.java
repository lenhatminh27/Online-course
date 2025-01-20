package com.course.web.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/blogs/*")
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
                req.getRequestDispatcher("/views/admin/create-blog.jsp").forward(req, resp);
                break;
            case "view":
                req.getRequestDispatcher("/views/admin/view-blogs.jsp").forward(req, resp);
                break;
            case "update":
                req.getRequestDispatcher("/views/admin/update-blog.jsp").forward(req, resp);
                break;
            case "delete":
                req.getRequestDispatcher("/views/admin/delete-blog.jsp").forward(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }
}

