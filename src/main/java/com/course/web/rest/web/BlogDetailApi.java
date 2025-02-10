package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.*;
import com.course.dao.impl.*;
import com.course.dto.response.BlogResponse;
import com.course.exceptions.NotFoundException;
import com.course.service.BlogService;
import com.course.service.impl.BlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/api/blog-post/*")
public class  BlogDetailApi extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final BlogService blogService;

    public BlogDetailApi() {
        BlogDAO blogDAO = new BlogDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        TagDAO tagDAO = new TagDAOImpl();
        BlogStatisticDAO blogStatisticDAO = new BlogStatisticDAOImpl();
        BlogCommentDAO blogCommentDAO = new BlogCommentDAOImpl();
        BookmarksBlogDAO bookmarksBlogDAO = new BookmarksBlogDAOImpl();
        SearchHistoryDAO searchHistoryDAO = new SearchHistoryDAOImpl();
        blogService = new BlogServiceImpl(blogDAO, accountDAO, tagDAO, blogStatisticDAO, blogCommentDAO, bookmarksBlogDAO,searchHistoryDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            String pathInfo = req.getPathInfo();
            String slug = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : null;
            if (ObjectUtils.isEmpty(slug)) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Không tìm thấy"));
                return;
            }
            BlogResponse response = blogService.findBlogBySlug(slug);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        }
        catch (NotFoundException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        }
        catch (Exception e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
