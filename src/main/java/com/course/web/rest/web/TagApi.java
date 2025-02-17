package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.TagResponse;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.TagService;
import com.course.service.impl.TagServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/tags/*")
public class TagApi extends BaseServlet {
    private static final long serialVersionUID = 1L;


    private TagService tagService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        tagService = getBean(TagServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
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
