package com.course.web.rest.admin;

import com.course.common.utils.ResponseUtils;
import com.course.dto.request.ReviewCourseDetailRequest;
import com.course.dto.response.BlogCommentResponse;
import com.course.dto.response.CourseResponse;
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
import java.io.Serial;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/review-course-detail/*")
public class ReviewCourseDetailAPI extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;
    private CourseService courseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        Long id = 0L;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id không hợp lệ"));
        }
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                CourseResponse courseResponse = courseService.findById(id);
                if (courseResponse.getStatus() != CourseStatus.IN_REVIEW) {
                    ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Khóa học không trong quá trình chờ xét duyệt"));
                    return;
                }
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(courseResponse));
            } catch (Exception e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Khóa học không tồn tại"));
            }
            return;
        }
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Điểm cuối không hợp lệ"));
    }


    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ReviewCourseDetailRequest reviewCourseDetailRequest = gson.fromJson(req.getReader(), ReviewCourseDetailRequest.class);
        if (reviewCourseDetailRequest.getEmail() == null || reviewCourseDetailRequest.getEmail().isEmpty()) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Email không hợp lệ"));
            return;
        }

        try {
            courseService.sendReviewCourseDetailEmail(reviewCourseDetailRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson(""));
        } catch (RuntimeException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Không thể gửi email: " + e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        Long courseId = 0L;

        // Extracting the course ID from the URL path
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                // Get the course ID from the URL
                courseId = Long.parseLong(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Course ID is not valid"));
                return;
            }
        }

        try {
            CourseResponse courseResponse = courseService.findById(courseId);

            if (courseResponse.getStatus() != CourseStatus.IN_REVIEW) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("The course is not in the review process"));
                return;
            }
            String action = req.getParameter("action");

            if ("accept".equalsIgnoreCase(action)) {
                courseService.acceptCourse(courseId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Khóa học được chấp nhận"));
            } else if ("reject".equalsIgnoreCase(action)) {
                courseService.rejectCourse(courseId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Khóa học bị từ chối"));
            } else {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid action. Action should be 'accept' or 'reject'"));
            }

        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Course not found with ID: " + courseId));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server error: " + e.getMessage()));
        }
    }

}
