package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.BlogStatisticDAO;
import com.course.dao.TagDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.BlogDAOImpl;
import com.course.dao.impl.BlogStatisticDAOImpl;
import com.course.dao.impl.TagDAOImpl;
import com.course.exceptions.NotFoundException;
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

@WebServlet(value = "/api/blogs-statistic/*")
public class BlogStatisticApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final BlogService blogService;

    public BlogStatisticApi() {
        BlogDAO blogDAO = new BlogDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        TagDAO tagDAO = new TagDAOImpl();
        BlogStatisticDAO blogStatisticDAO = new BlogStatisticDAOImpl();
        blogService = new BlogServiceImpl(blogDAO, accountDAO, tagDAO, blogStatisticDAO);
    }

    @Override
    @IsAuthenticated
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // Get the path after /api/blogs-statistic/
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                handleLikeBlog(resp, id);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid ID format"));
            }
            return;
        }
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid endpoint"));
    }


    private void handleLikeBlog(HttpServletResponse resp, Long id) throws IOException {
        try{
            blogService.likeBlog(id);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson(""));
        }
        catch (NotFoundException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        }
        catch (Exception e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
