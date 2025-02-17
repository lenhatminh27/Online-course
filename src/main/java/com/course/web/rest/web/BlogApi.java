package com.course.web.rest.web;

import com.course.common.constants.PageConstant;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.core.repository.data.Sort;
import com.course.dao.*;
import com.course.dao.impl.*;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.request.BlogFilterRequest;
import com.course.dto.request.BlogUpdateRequest;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.PageResponse;
import com.course.exceptions.AuthenticationException;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.BlogService;
import com.course.service.impl.BlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/blogs/*")
public class BlogApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private BlogService blogService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        blogService = getBean(BlogServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/recent")) {
            handleGetRecentBlogs(resp);
            return;
        }
        String pageStr = req.getParameter("page");
        String search = req.getParameter("search");
        int page;
        try {
            page = (pageStr != null) ? Integer.parseInt(pageStr) : PageConstant.PAGE_CURRENT;
        } catch (NumberFormatException e) {
            page = PageConstant.PAGE_CURRENT;
        }
        String tagsParam = req.getParameter("tags");
        List<String> tags = new ArrayList<>();
        if (tagsParam != null && !tagsParam.trim().isEmpty()) {
            tags = Arrays.asList(tagsParam.split(","));
        }
        String sort = req.getParameter("sort");
        List<Sort.Order> orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(sort)) {
            if (sort.equalsIgnoreCase("newest")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "createAt"));
            }
            if (sort.equalsIgnoreCase("oldest")) {
                orders.add(new Sort.Order(Sort.Direction.ASC, "createAt"));
            }
            if (sort.equalsIgnoreCase("views")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "views"));
            }
            if (sort.equalsIgnoreCase("likes")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "likes"));
            }
        }
        Sort sortT = null;
        if (!ObjectUtils.isEmpty(orders)) {
            sortT = Sort.by(orders);
        }
        BlogFilterRequest blogFilterRequest = new BlogFilterRequest(tags, sortT, search, page, null);
        if (pathInfo != null && pathInfo.equals("/instructor")) {
            handleGetInstuctorBlogs(resp, blogFilterRequest);
            return;
        }
        try {
            PageResponse<BlogResponse> blogResponse = blogService.getBlogs(blogFilterRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(blogResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    private void handleGetRecentBlogs(HttpServletResponse resp) throws IOException {
        try {
            List<BlogResponse> recentBlogs = blogService.getTopBlogRecent();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(recentBlogs));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }

    }

    private void handleGetInstuctorBlogs(HttpServletResponse resp, BlogFilterRequest blogFilterRequest) throws IOException {
        try {
            PageResponse<BlogResponse> blogsByInstructor = blogService.getBlogsByInstructor(blogFilterRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(blogsByInstructor));
        } catch (AuthenticationException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }


    @Override
    @IsAuthenticated
    @HasPermission("CREATE_BLOG")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogCreateRequest blogCreateRequest = gson.fromJson(req.getReader(), BlogCreateRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(blogCreateRequest)) {
            errors.add("Yêu cầu không được để trống");
        }

        if (ObjectUtils.isEmpty(blogCreateRequest.getTitle())) {
            errors.add("Tiêu đề không được để trống");
        }
        if (ObjectUtils.isEmpty(blogCreateRequest.getContent())) {
            errors.add("Nội dung không được để trống");
        }

        if (ObjectUtils.isEmpty(blogCreateRequest.getTagName())) {
            errors.add("Tag không được để trống");
        }

        if (blogService.existTitle(blogCreateRequest.getTitle())) {
            errors.add("Tiêu đề đã tồn tại");
        }


        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }

        try {
            BlogResponse blogResponse = blogService.createBlog(blogCreateRequest);

            // Trả về phản hồi thành công với blog vừa tạo
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(blogResponse));
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi server
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server");
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_BLOG")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BlogUpdateRequest blogUpdateRequest = gson.fromJson(req.getReader(), BlogUpdateRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(blogUpdateRequest.getTitle())) {
            errors.add("không được để trống tiêu đề.");
        }
        if (ObjectUtils.isEmpty(blogUpdateRequest.getContent())) {
            errors.add("không được để trống nội dung.");
        }
        if (ObjectUtils.isEmpty(blogUpdateRequest.getTagName())) {
            errors.add("không được để trống tag.");
        }
        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        String pathInfo = req.getPathInfo(); // lấy đường dẫn của blog
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của blog không được để trống"));
            return;
        }
        Long BlogId; // chuyển blogId sang dạng Long
        try {
            BlogId = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của blog không hợp lệ"));
            return;
        }

        // update blog
        try {
            blogService.updateBlog(BlogId, blogUpdateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("cập nhật blog thành công"));
        }
        catch (BadRequestException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getError()));
        }
        catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("DELETE_BLOG")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của blog không được để trống"));
            return;
        }
        Long BlogId;
        try {
            BlogId = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của blog không hợp lệ"));
            return;
        }
        try {
            blogService.deleteBlog(BlogId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Xóa blog thành công"));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }
    }
}