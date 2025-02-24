package com.course.web.rest.web;

import com.course.common.constants.PageConstant;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.core.repository.data.Sort;
import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.request.CreateCourseRequest;
import com.course.dto.request.UpdateCourseRequest;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.PageResponse;
import com.course.exceptions.ForbiddenException;
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

@WebServlet("/api/course/*")
public class CourseApi extends BaseServlet {

    private CourseService courseService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = getBean(CourseServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("INSTRUCTOR")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            String pageStr = req.getParameter("page");
            String sizeStr = req.getParameter("size");
            String search = req.getParameter("search");
            int page;
            try {
                page = !ObjectUtils.isEmpty(pageStr) ? Integer.parseInt(pageStr) : PageConstant.PAGE_CURRENT;
            } catch (NumberFormatException e) {
                page = PageConstant.PAGE_CURRENT;
            }
            int size;
            try {
                size = !ObjectUtils.isEmpty(sizeStr) ? Integer.parseInt(sizeStr) : PageConstant.PAGE_LIMIT;
            } catch (NumberFormatException e) {
                size = PageConstant.PAGE_LIMIT;
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
                if (sort.equalsIgnoreCase("A-Z")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "title"));
                }
                if (sort.equalsIgnoreCase("Z-A")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "title"));
                }
            }
            Sort sortT = null;
            if (!ObjectUtils.isEmpty(orders)) {
                sortT = Sort.by(orders);
            }
            CourseInstructorFilterRequest filterRequest = new CourseInstructorFilterRequest(sortT, search, page, size);
            PageResponse<CourseResponse> response = courseService.getAllListCourseByUserCurrent(filterRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Có lỗi xảy ra!"));
        }
    }

    @Override
    @IsAuthenticated
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

    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_COURSE")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            UpdateCourseRequest updateCourseRequest = gson.fromJson(req.getReader(), UpdateCourseRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(updateCourseRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(updateCourseRequest.getCourseId())) {
                errors.add("Id khóa học không được để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            CourseResponse courseResponse = courseService.updateCourse(updateCourseRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(courseResponse));
        }catch (NotFoundException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Có lỗi xảy ra!"));
        }
    }

}
