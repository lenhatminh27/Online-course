package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.CreateSectionRequest;
import com.course.dto.request.UpdateSectionRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.SectionResponse;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.SectionService;
import com.course.service.impl.SectionServiceImpl;
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


@WebServlet("/api/section/*")
public class SectionApi extends BaseServlet {

    private Gson gson;

    private SectionService sectionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        sectionService = getBean(SectionServiceImpl.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            // Lấy courseID từ URL, bỏ dấu "/"
            String courseIdStr = pathInfo.substring(1);
            try {
                Long courseId = Long.parseLong(courseIdStr);
                handleGetSectionsByCourse(courseId, resp);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid number"));
            }
            return;
        }
    }

    private void handleGetSectionsByCourse(Long courseId, HttpServletResponse resp) throws IOException {
        try{
            List<SectionResponse> response = sectionService.getSectionByCourseId(courseId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        }catch (NotFoundException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (ForbiddenException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("CREATE_COURSE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            CreateSectionRequest createSectionRequest = gson.fromJson(req.getReader(), CreateSectionRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(createSectionRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(createSectionRequest.getTitle())) {
                errors.add("Tiêu đề không được để trống");
            }
            if (ObjectUtils.isEmpty(createSectionRequest.getCourseId())) {
                errors.add("Id khóa học không dược để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            SectionResponse sectionResponse = sectionService.creatSection(createSectionRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(sectionResponse));
        } catch (NotFoundException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (BadRequestException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getError()));
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
            UpdateSectionRequest updateSectionRequest = gson.fromJson(req.getReader(), UpdateSectionRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(updateSectionRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(updateSectionRequest.getSectionId())) {
                errors.add("Id của phần học không được để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            SectionResponse sectionResponse = sectionService.updateSection(updateSectionRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(sectionResponse));
        } catch (NotFoundException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
        catch (BadRequestException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getError()));
        }
        catch (ForbiddenException e){
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Có lỗi xảy ra!"));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("DELETE_SECTION")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String sectionIdStr = pathInfo.substring(1);
            try {
                Long sectionId = Long.parseLong(sectionIdStr);
                sectionService.deleteSection(sectionId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("Xóa section thành công"));
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid number"));
            } catch (NotFoundException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
            } catch (ForbiddenException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
            }
        }
    }
}
