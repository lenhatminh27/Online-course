package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.LessonDAO;
import com.course.dao.impl.LessonDAOImpl;
import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.request.UpdateLessonCommentRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonCommentResponse;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.LessonCommentService;
import com.course.service.impl.LessonCommentServiceImpl;
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

@WebServlet("/api/lesson-comment/*")
public class LessonCommentApi extends BaseServlet {

    private LessonCommentService lessonCommentService;

    private LessonDAO lessonDAO;

    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        lessonCommentService = getBean(LessonCommentServiceImpl.class.getSimpleName());
        lessonDAO = getBean(LessonDAOImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("CREATE_COMMENT_LESSON")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        CreateLessonCommentRequest request = gson.fromJson(req.getReader(), CreateLessonCommentRequest.class);
        List<String> errors = new ArrayList<>();
        if(ObjectUtils.isEmpty(request.getContent())) {
            errors.add("Nội dung bình luận không được để trống!");
        }
        if(!ObjectUtils.isEmpty(errors)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        if (lessonDAO.findById(request.getLessonId()) == null) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Lesson không tồn tại"));
            return;
        }
        try {
            LessonCommentResponse response = lessonCommentService.createLessonComment(request);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(response));
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Server Error"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.startsWith("/")) {
            try {
                Long lessonId = Long.parseLong(pathInfo.substring(1));
                List<LessonCommentResponse> response = lessonCommentService.getLessonCommentByLessonId(lessonId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
            }
            catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Định dạng ID không đúng!"));
            }
            catch (Exception e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
            }
        }
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Điểm cuối không hợp lệ"));
    }

    @Override
    @IsAuthenticated
    @HasPermission("UPDATE_COMMENT_LESSON")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        UpdateLessonCommentRequest request = gson.fromJson(req.getReader(), UpdateLessonCommentRequest.class);
        List<String> errors = new ArrayList<>();
        if (ObjectUtils.isEmpty(request.getContent())) {
            errors.add("Nội dung bình luận không được để trống!");
        }
        if (!ObjectUtils.isEmpty(errors)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
        }
        try{
            LessonCommentResponse response = lessonCommentService.updateLessonComment(request);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("DELETE_COMMENT_LESSON")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.startsWith("/")) {
            try {
                String commentIdString = pathInfo.substring(1);
                Long commentId = Long.parseLong(commentIdString);
                lessonCommentService.deleteLessonComment(commentId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Xóa comment thành công"));
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Định dạng ID không đúng!"));
            } catch (NotFoundException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson(e));
            } catch (ForbiddenException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e));
            } catch (Exception e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Server Error"));
            }
        }
    }
}
