package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.LessonDAO;
import com.course.dao.impl.LessonDAOImpl;
import com.course.dto.request.CreateLessonCommentRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.LessonCommentResponse;
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

@WebServlet("/api/lesson-comment")
public class LessonCommentApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

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
        if (request.getContent().length() > 500) {
            errors.add("Nội dung không được quá 500 ký tự");
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
}
