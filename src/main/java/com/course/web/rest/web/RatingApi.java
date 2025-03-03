package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.RatingDAO;
import com.course.dao.CourseDAO;
import com.course.dao.impl.CourseDAOImpl;
import com.course.dao.impl.RatingDaoImpl;
import com.course.dto.request.RatingCreateRequest;
import com.course.dto.request.RatingUpdateRequest;
import com.course.dto.response.RatingResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.exceptions.RatingException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.RatingService;
import com.course.service.impl.RatingServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/rating/*")
public class RatingApi extends BaseServlet {

    private Gson gson;
    private RatingService ratingService;
    private RatingDAO ratingDAO;
    private CourseDAO courseDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ratingService = getBean(RatingServiceImpl.class.getSimpleName());
        ratingDAO = getBean(RatingDaoImpl.class.getSimpleName());
        courseDAO = getBean(CourseDAOImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.startsWith("/top3")) {
            try {
                long courseId = Long.parseLong(pathInfo.substring(6));
                List<RatingResponse> top3Rating = ratingService.getTop3Rating(courseId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(top3Rating));
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Course ID không hợp lệ"));
            }
            return;
        }


        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                long courseId = Long.parseLong(pathInfo.substring(1));
                List<RatingResponse> ratings = ratingService.findRatingByCourse(courseId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(ratings));
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Course ID không hợp lệ"));
            } catch (Exception e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
            }
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("RATING_COURSE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        RatingCreateRequest ratingCreateRequest = gson.fromJson(req.getReader(), RatingCreateRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(ratingCreateRequest)) {
            errors.add("Yêu cầu không được để trống");
        }
        if (ObjectUtils.isEmpty(ratingCreateRequest.getCourseId())) {
            errors.add("Id của khóa học không được để trống");
        }
        if (ObjectUtils.isEmpty(ratingCreateRequest.getRating())) {
            errors.add("Điểm đánh giá không được để trống");
        }
        if (ratingCreateRequest.getRating() < 1 || ratingCreateRequest.getRating() > 5) {
            errors.add("Điểm đánh giá phải từ 1 đến 5");
        }

        if (ObjectUtils.isEmpty(ratingCreateRequest.getReview())) {
            errors.add("Nội dung không được để trống");
        }

        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }

        if (courseDAO.findById(ratingCreateRequest.getCourseId()) == null) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Khóa học không tồn tại"));
            return;
        }

        try {
            RatingResponse ratingResponse = ratingService.createRating(ratingCreateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(ratingResponse));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        } catch (RatingException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi Server");
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("RATING_COURSE")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của rating không hợp lệ"));
            return;
        }
        Long ratingId;
        try {
            ratingId = Long.parseLong(pathInfo.substring(1));  // Lấy ID từ URL
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của rating không hợp lệ"));
            return;
        }
        RatingUpdateRequest ratingUpdateRequest = gson.fromJson(req.getReader(), RatingUpdateRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(ratingUpdateRequest)) {
            errors.add("Yêu cầu không được để trống");
        }
        if (ObjectUtils.isEmpty(ratingUpdateRequest.getReview())) {
            errors.add("Nội dung không được để trống");
        }
        if (ObjectUtils.isEmpty(ratingUpdateRequest.getRating())) {
            errors.add("Số sao không được để trống");
        }

        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }

        try {
            RatingResponse ratingResponse = ratingService.updateRating(ratingId, ratingUpdateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(ratingResponse));
        } catch (RatingException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getMessage()));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }
    }


    @Override
    @IsAuthenticated
    @HasPermission("RATING_COURSE")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của rating không được để trống"));
            return;
        }

        Long ratingId;
        try {
            ratingId = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của rating không hợp lệ"));
            return;
        }

        try {
            ratingService.deleteRating(ratingId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson(""));
        } catch (RatingException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getMessage()));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }
    }
}
