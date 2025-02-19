package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.CreateCourseRequest;
import com.course.dto.request.RegisterRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.CourseService;
import com.course.service.impl.CourseServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/course")
public class CourseApi extends BaseServlet {

    private CourseService courseService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @HasPermission("CREATE_COURSE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            CreateCourseRequest createCourseRequest = gson.fromJson(req.getReader(), CreateCourseRequest.class);
            List<String> errors = new ArrayList<>();

            if (ObjectUtils.isEmpty(createCourseRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(createCourseRequest.getTitle())) {
                errors.add("Tiêu đề không được để trống");
            }
            if (ObjectUtils.isEmpty(createCourseRequest.getCategoriesId())) {
                errors.add("Id danh mục không dược để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            CourseResponse newCourse = courseService.createCourse(createCourseRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(newCourse));
        }
        catch (NotFoundException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Có lỗi xảy ra!"));
        }
    }
}
