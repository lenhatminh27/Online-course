package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.request.SearchInCourseRequest;
import com.course.dto.response.SearchInCourseResponse;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.SearchInCourseService;
import com.course.service.impl.BlogServiceImpl;
import com.course.service.impl.SearchInCourseServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/search-in-course")
public class SearchInCourseApi extends BaseServlet {

    private SearchInCourseService searchInCourseService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        searchInCourseService = getBean(SearchInCourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        SearchInCourseRequest searchInCourseRequest = gson.fromJson(req.getReader(), SearchInCourseRequest.class);
        try {
            SearchInCourseResponse searchInCourseResponse = searchInCourseService.searchInCourse(searchInCourseRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(searchInCourseResponse));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
    }
}
