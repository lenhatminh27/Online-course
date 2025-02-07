package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.BookmarksBlogDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.BlogDAOImpl;
import com.course.dao.impl.BookmarksBlogDAOImpl;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.BookmarksBlogService;
import com.course.service.impl.BookmarksBlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/bookmarks/*")
public class BookmarksBlogApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final BookmarksBlogService bookmarksBlogService;

    public BookmarksBlogApi() {
        BookmarksBlogDAO bookmarksBlogDAO = new BookmarksBlogDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        BlogDAO blogDAO = new BlogDAOImpl();
        bookmarksBlogService = new BookmarksBlogServiceImpl(bookmarksBlogDAO, blogDAO, accountDAO);
    }

    @Override
    @IsAuthenticated
    @HasPermission("BOOKMARK_BLOG")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BookmarksBlogRequest bookmarksBlogRequest = gson.fromJson(req.getReader(), BookmarksBlogRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(bookmarksBlogRequest)) {
            errors.add("Request không được null");
        }

        if (ObjectUtils.isEmpty(bookmarksBlogRequest.getBlogId())) {
            errors.add("BlogId không được null");
        }

        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }


        try {
            bookmarksBlogService.createBookmarksBlog(bookmarksBlogRequest);
            // Trả về phản hồi thành công với blog vừa tạo
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson(""));
        }
        catch (NotFoundException ex){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Server Error");

        }
        catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi server
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
