package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.ToInstructorService;
import com.course.service.impl.ToInstructorServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/api/to-instructor")
public class ToInstructorApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final ToInstructorService toInstructorService;

    public ToInstructorApi() {
        AccountDAO accountDAO = new AccountDaoImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        this.toInstructorService = new ToInstructorServiceImpl(accountDAO, roleDAO);
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
