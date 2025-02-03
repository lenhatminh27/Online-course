package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.TagDAO;
import com.course.dao.impl.TagDAOImpl;
import com.course.dto.response.TagResponse;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.TagService;
import com.course.service.impl.TagServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/tags/*")
public class TagApi extends BaseServlet {
    private static final long serialVersionUID = 1L;


    private final TagService tagService;

    private final Gson gson = new Gson();

    public TagApi() {
        TagDAO tagDAO = new TagDAOImpl();
        this.tagService = new TagServiceImpl(tagDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/recent")) {
            handleGetRecentTags(resp);
            return;
        }
    }

    private void handleGetRecentTags(HttpServletResponse resp) throws IOException {
        try {
            List<TagResponse> recentBlogs = tagService.findTagsRecent();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(recentBlogs));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
