package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.UpdateCourseRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.RatingResponse;
import com.course.entity.enums.CourseStatus;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
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

@WebServlet("/api/courseToReview/*")
public class ToReviewCourseApi extends BaseServlet {
    private CourseService courseService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_COURSE")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của course không hợp lệ"));
            return;
        }

        String courseIdStr = pathInfo.substring(1);
        Long courseId;
        try {
            courseId = Long.parseLong(courseIdStr);
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của course phải là một số hợp lệ"));
            return;
        }

        try {
            courseService.updateStatus(courseId, CourseStatus.IN_REVIEW);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson(""));
        } catch (NotFoundException e) {
            // Handle course not found error
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Không tìm thấy khóa học với ID: " + courseId));
        } catch (Exception e) {
            // Handle any other exceptions
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Đã có lỗi xảy ra khi cập nhật trạng thái khóa học"));
        }
    }
}



