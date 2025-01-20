package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.TagDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.BlogDAOImpl;
import com.course.dao.impl.TagDAOImpl;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.ErrorResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.BlogService;
import com.course.service.impl.BlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/blogs/*")

public class BlogApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final BlogService blogService;

    public BlogApi() {
        BlogDAO blogDAO = new BlogDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        TagDAO tagDAO = new TagDAOImpl();
        blogService = new BlogServiceImpl(blogDAO, accountDAO, tagDAO);
    }

    @Override
    @IsAuthenticated
    @HasPermission("CREATE_BLOG")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BlogCreateRequest blogCreateRequest = gson.fromJson(req.getReader(), BlogCreateRequest.class);
        List<String> errors = new ArrayList<>();
        List<String> errors2 = new ArrayList<>();

        if (ObjectUtils.isEmpty(blogCreateRequest)) {
            errors.add("Request can not null");
        }

        if (ObjectUtils.isEmpty(blogCreateRequest.getTitle())) {
            errors.add("Title can not null");
        }
        if (ObjectUtils.isEmpty(blogCreateRequest.getContent())) {
            errors.add("Content can not null");
        }

        if (ObjectUtils.isEmpty(blogCreateRequest.getTagName())) {
            errors.add("Tag name can not null");
        }

        if (blogService.existTitle(blogCreateRequest.getTitle())) {
            errors.add("Title already exists");
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
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }


    }
}