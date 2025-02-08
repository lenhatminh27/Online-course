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
import com.course.dto.request.BlogCommentUpdateRequest;
import com.course.dto.response.BlogCommentResponse;
import com.course.dto.response.ErrorResponse;
import com.course.entity.BlogCommentEntity;
import com.course.exceptions.ForbiddenException;
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

    private final AccountDAO accountDAO;

    private final BlogCommentDAO blogCommentDAO;

    private final BlogCommentService blogCommentService;

    public BlogCommentApi() {
        blogDAO = new BlogDAOImpl();
        blogCommentDAO = new BlogCommentDAOImpl();
        accountDAO = new AccountDaoImpl();
        blogCommentService = new BlogCommentServiceImpl(blogDAO, accountDAO, blogCommentDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                String slug = pathInfo.substring(1);
                List<BlogCommentResponse> listComment = blogCommentService.getListCommentByBlogSlug(slug);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(listComment));
            }
            catch (Exception e){
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server error"));
            }
            return;
        }
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Điểm cuối không hợp lệ"));
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

    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_COMMENT_BLOG")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        BlogCommentUpdateRequest blogCommentUpdateRequest = gson.fromJson(req.getReader(), BlogCommentUpdateRequest.class);

        List<String> errors = new ArrayList<>();
        if (ObjectUtils.isEmpty(blogCommentUpdateRequest)) {
            errors.add("Yêu cầu không được để trống");
        }
        if (ObjectUtils.isEmpty(blogCommentUpdateRequest.getId())) {
            errors.add("Id của comment không được để trống");
        }
        if (ObjectUtils.isEmpty(blogCommentUpdateRequest.getContent())) {
            errors.add("Nội dung không được để trống");
        }

        if (!ObjectUtils.isEmpty(errors)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        if (blogCommentDAO.findBlogCommentById(blogCommentUpdateRequest.getId()) == null) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Comment không tồn tại"));
            return;
        }

        try {
            BlogCommentResponse blogCommentResponse = blogCommentService.updateBlogComment(blogCommentUpdateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(blogCommentResponse));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("DELETE_COMMENT_BLOG")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo(); // lấy đường dẫn của comment
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của comment không được để trống"));
            return;
        }

        Long commentId; // chuyển commentId sang dạng Long
        try {
            commentId = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của comment không hợp lệ"));
            return;
        }

        // thực hiện việc xóa comment
        try {
            blogCommentService.deleteBlogComment(commentId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Xóa comment thành công"));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
        }
    }
}
