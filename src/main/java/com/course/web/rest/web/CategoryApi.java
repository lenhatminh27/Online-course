package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.CategoryResponse;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.CategoryService;
import com.course.service.impl.CategoryServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/category/*")
public class CategoryApi extends BaseServlet {

    private Gson gson;

    private CategoryService categoryService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        categoryService = getBean(CategoryServiceImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/no-parent")) {
            handleGetAllNoParent(resp);
            return;
        }
    }

    private void handleGetAllNoParent(HttpServletResponse resp) throws IOException {
        try {
            List<CategoryResponse> res = categoryService.getAllCategoriesParent();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
