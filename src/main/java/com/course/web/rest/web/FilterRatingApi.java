package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.request.FilterRatingRequest;
import com.course.dto.response.RatingResponse;
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
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;


@WebServlet("/api/filterRating/*")
public class FilterRatingApi extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson;
    private RatingService ratingService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ratingService = getBean(RatingServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Lấy phần pathInfo từ URL để xác định courseId
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Course ID không hợp lệ"));
            return;
        }

        // Lấy courseId từ phần đường dẫn URL (sau dấu "/")
        String courseIdParam = pathInfo.substring(1); // Xóa "/" ở đầu
        Long courseId = null;
        try {
            courseId = Long.parseLong(courseIdParam); // Chuyển courseId sang số nguyên
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Course ID phải là số"));
            return;
        }

        // Lấy các tham số filter từ query parameters
        String reviewFilter = req.getParameter("review");  // Lọc theo nội dung review
        String ratingFilter = req.getParameter("rating");  // Lọc theo sao (rating)

        // Tạo FilterRatingRequest từ các tham số query
        FilterRatingRequest filterRatingRequest = new FilterRatingRequest();
        filterRatingRequest.setCourseId(courseId);  // Đặt courseId vào request filter

        // Thêm các tham số lọc khác (review và rating)
        if (reviewFilter != null && !reviewFilter.isEmpty()) {
            filterRatingRequest.setReview(reviewFilter); // Nếu review có giá trị, thêm vào request
        }
        if (ratingFilter != null && !ratingFilter.isEmpty()) {
            try {
                int rating = Integer.parseInt(ratingFilter); // Chuyển rating sang số nguyên
                filterRatingRequest.setRating(rating); // Nếu rating có giá trị, thêm vào request
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Rating phải là số nguyên"));
                return;
            }
        }

        // Gọi service để lấy các đánh giá theo điều kiện filter
        try {
            List<RatingResponse> ratings = ratingService.getRatingByReviewAndRatingAndCourseId(filterRatingRequest);

            // Trả về kết quả dưới dạng JSON
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(ratings));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }
    }

}

