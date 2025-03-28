package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.dto.response.BookmarksBlogResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.BookmarksBlogService;
import com.course.service.impl.BookmarksBlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/bookmarks/*")
public class BookmarksBlogApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private BookmarksBlogService bookmarksBlogService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        bookmarksBlogService = getBean(BookmarksBlogServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
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

    @Override
    @IsAuthenticated
    @HasPermission("BOOKMARK_BLOG")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Gọi service để lấy danh sách blog đã bookmark
            List<BookmarksBlogResponse> bookmarksBlogResponses = bookmarksBlogService.getBookmarkedBlogs();

            // Kiểm tra nếu danh sách rỗng
            if (ObjectUtils.isEmpty(bookmarksBlogResponses)) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("Không có blog nào được bookmark"));
                return;
            }

            // Ghi phản hồi JSON về client
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(bookmarksBlogResponses));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("BOOKMARK_BLOG")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BookmarksBlogRequest bookmarksBlogRequest = gson.fromJson(req.getReader(), BookmarksBlogRequest.class);
        if (ObjectUtils.isEmpty(bookmarksBlogRequest)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(List.of("BlogId không được null"));
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }

        try {
            Long blogId = bookmarksBlogRequest.getBlogId();
            bookmarksBlogService.deleteBookmark(blogId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "");
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("BlogId không hợp lệ"));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Bookmark không tồn tại"));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
        }
    }
}
