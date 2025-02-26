package com.course.web.rest.admin;

import com.course.common.constants.PageConstant;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.core.repository.data.Sort;
import com.course.dto.request.ReviewCourseFilterRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;
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

@WebServlet("/api/review-course/*")
public class ReviewCourseApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private Gson gson;
    private CourseService courseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @IsAuthenticated
    @Override
    @HasPermission("ADMIN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String pageStr = req.getParameter("page");
            String search = req.getParameter("search");

            int page;

            try {
                page = (pageStr != null) ? Integer.parseInt(pageStr) : PageConstant.PAGE_CURRENT;
            } catch (NumberFormatException e) {
                page = PageConstant.PAGE_CURRENT;
            }

            String sort = req.getParameter("sort");
            List<Sort.Order> orders = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sort)) {
                if (sort.equalsIgnoreCase("newest")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
                }
                if (sort.equalsIgnoreCase("oldest")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, "createdAt"));
                }
                if (sort.equalsIgnoreCase("increase-price")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, "price"));
                }
                if (sort.equalsIgnoreCase("decrease-price")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "price"));
                }
            }
            Sort sortT = null;
            if (!ObjectUtils.isEmpty(orders)) {
                sortT = Sort.by(orders);
            }
            ReviewCourseFilterRequest filterRequest = new ReviewCourseFilterRequest(search, page, sortT);

            PageResponse<CourseResponse> response = courseService.getInReviewCourse(filterRequest);

            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}



