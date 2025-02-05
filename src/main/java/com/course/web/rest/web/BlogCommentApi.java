package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogCommentDAO;
import com.course.dao.BlogDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.BlogCommentDAOImpl;
import com.course.dao.impl.BlogDAOImpl;
import com.course.dto.request.BlogCommentCreateRequest;
import com.course.dto.response.BlogCommentResponse;
import com.course.dto.response.ErrorResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.BlogCommentService;
import com.course.service.impl.BlogCommentServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/blog-comment/*")
public class BlogCommentApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final BlogDAO blogDAO;

    private final BlogCommentService blogCommentService;

    public BlogCommentApi() {
        blogDAO = new BlogDAOImpl();
        BlogCommentDAO blogCommentDAO = new BlogCommentDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        blogCommentService = new BlogCommentServiceImpl(blogDAO, accountDAO, blogCommentDAO);
    }

    

    @Override
    @IsAuthenticated
    @HasPermission("CREATE_COMMENT_BLOG")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BlogCommentCreateRequest blogCommentCreateRequest = gson.fromJson(req.getReader(), BlogCommentCreateRequest.class);
        List<String> errors = new ArrayList<>();
        if (ObjectUtils.isEmpty(blogCommentCreateRequest)) {
            errors.add("Yêu cầu không được để trống");
        }
        if (ObjectUtils.isEmpty(blogCommentCreateRequest.getBlogId())) {
            errors.add("Id của bài viết không được để trống");
        }
        if (ObjectUtils.isEmpty(blogCommentCreateRequest.getContent())) {
            errors.add("Nội dung không được để trống");
        }
        if (!ObjectUtils.isEmpty(errors)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        if (blogDAO.findBlogById(blogCommentCreateRequest.getBlogId()) == null) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Blog không tồn tại"));
            return;
        }

        try {
            BlogCommentResponse blogCommentResponse = blogCommentService.createBlogComment(blogCommentCreateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(blogCommentResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
