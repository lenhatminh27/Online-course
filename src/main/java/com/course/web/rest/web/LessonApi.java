package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.CreateLessonRequest;
import com.course.dto.request.UpdateLessonRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonResponse;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.LessonService;
import com.course.service.impl.LessonServiceImpl;
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

@WebServlet("/api/lesson")
public class LessonApi extends BaseServlet {

    private LessonService lessonService;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        lessonService = getBean(LessonServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }


    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_COURSE")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            UpdateLessonRequest updateLessonRequest = gson.fromJson(req.getReader(), UpdateLessonRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(updateLessonRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(updateLessonRequest.getLessonId())) {
                errors.add("Id của bài hoc không được để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            LessonResponse lessonResponse = lessonService.updateLesson(updateLessonRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(lessonResponse));
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
    @HasPermission("CREATE_COURSE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            CreateLessonRequest createLessonRequest = gson.fromJson(req.getReader(), CreateLessonRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(createLessonRequest)) {
                errors.add("Request không được để trống");
            }
            if (ObjectUtils.isEmpty(createLessonRequest.getTitle())) {
                errors.add("Tiêu đề không được để trống");
            }
            if (ObjectUtils.isEmpty(createLessonRequest.getSectionId())) {
                errors.add("Id phần học không dược để trống");
            }
            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            LessonResponse lessonResponse = lessonService.createLesson(createLessonRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(lessonResponse));
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
}
