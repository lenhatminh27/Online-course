package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.ToInstructorService;
import com.course.service.impl.ToInstructorServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/to-instructor")
public class ToInstructorApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private ToInstructorService toInstructorService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        toInstructorService = getBean(ToInstructorServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("LEARNER")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            this.toInstructorService.updateRole();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "");
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error");
        }
    }
}
