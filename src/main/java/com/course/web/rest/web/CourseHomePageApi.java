package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.WishlistCourseRespone;
import com.course.entity.CourseEntity;
import com.course.exceptions.ForbiddenException;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.CourseService;
import com.course.service.WishlistService;
import com.course.service.impl.CourseServiceImpl;
import com.course.service.impl.WishlistServiceImpl;
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

@WebServlet("/api/view-course-home-page")
public class CourseHomePageApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private CourseService courseService;

    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            List<CourseResponse> list = courseService.getTop3Course();

            if (ObjectUtils.isEmpty(list)) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("không có khóa học nào đang sẵn sàng"));
                return;
            }

            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(list));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }
}