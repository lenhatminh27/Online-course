package com.course.web.rest.admin;

import com.course.common.utils.ResponseUtils;
import com.course.dao.PermissionDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.PermissionDAOImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.dto.response.RoleResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AuthorizationService;
import com.course.service.impl.AuthorizationServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/roles")
public class RoleApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private AuthorizationService authorizationService;

    public RoleApi() {
        PermissionDAO permissionDAO = new PermissionDAOImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        this.authorizationService = new AuthorizationServiceImpl(permissionDAO, roleDAO);
    }

    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        List<RoleResponse> roles = this.authorizationService.getAllRole();
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(roles));
    }
}
