package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.response.BlogResponse;
import com.course.exceptions.NotFoundException;
import com.course.service.BlogService;
import com.course.service.impl.BlogServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/blog-post/*")
public class  BlogDetailApi extends HttpServlet {

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
