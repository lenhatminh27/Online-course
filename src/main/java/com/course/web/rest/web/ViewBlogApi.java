package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
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

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet(value = "/api/view-blog/*")
public class ViewBlogApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private BlogService blogService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        blogService = getBean(BlogServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                blogService.viewBlog(id);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Định dạng ID không hợp lệ"));
            }
            return;
        }
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Điểm cuối không hợp lệ"));
    }

}
