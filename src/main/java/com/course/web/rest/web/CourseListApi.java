package com.course.web.rest.web;

import com.course.common.constants.PageConstant;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.core.repository.data.Sort;
import com.course.dto.request.CourseFilterRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.CourseService;
import com.course.service.impl.CourseServiceImpl;
import com.google.gson.Gson;
import com.google.protobuf.Api;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/view-course-list/*")
public class CourseListApi extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private CourseService courseService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            int page;
            int size;

            String pageRaw = req.getParameter("page");
            String sizeRaw = req.getParameter("pageSize");
            String search = req.getParameter("search");
            String sort = req.getParameter("sort");
            String category = req.getParameter("category");

            try {
                page = !ObjectUtils.isEmpty(pageRaw) ? Integer.parseInt(pageRaw) : PageConstant.PAGE_CURRENT;
            }catch (NumberFormatException e) {
                page = PageConstant.PAGE_CURRENT;
            }

            try {
                size = !ObjectUtils.isEmpty(sizeRaw) ? Integer.parseInt(sizeRaw) : PageConstant.PAGE_LIMIT;
            } catch (NumberFormatException e) {
                size = PageConstant.PAGE_LIMIT;
            }

            List<Sort.Order> orders = new ArrayList<>();

            if (!ObjectUtils.isEmpty(sort)) {
                if (sort.equalsIgnoreCase("newest")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
                }
                if (sort.equalsIgnoreCase("oldest")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, "createdAt"));
                }
                if (sort.equalsIgnoreCase("A-Z")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "title"));
                }
                if (sort.equalsIgnoreCase("Z-A")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "title"));
                }
            }

            Sort sortType = null;
            if (!ObjectUtils.isEmpty(orders)) {
                sortType = Sort.by(orders);
            }

            CourseFilterRequest filterRequest = new CourseFilterRequest(sortType, search,page, size);
            PageResponse<CourseResponse> response = courseService.getAllCoursePublic(filterRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }
}
