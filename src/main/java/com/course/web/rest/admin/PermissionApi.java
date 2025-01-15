package com.course.web.rest.admin;

import com.course.common.utils.ResponseUtils;
import com.course.dao.PermissionDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.PermissionDAOImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.dto.request.PermissionRequest;
import com.course.dto.response.PermissionResponse;
import com.course.dto.response.RoleResponse;
import com.course.entity.RoleEntity;
import com.course.exceptions.NotFoundException;
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

@WebServlet("/api/permissions/*")
public class PermissionApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private AuthorizationService authorizationService;

    public PermissionApi() {
        PermissionDAO permissionDAO = new PermissionDAOImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        this.authorizationService = new AuthorizationServiceImpl(permissionDAO, roleDAO);
    }

    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.split("/").length != 2) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing role ID in the URL.");
                return;
            }
            String idStr = pathInfo.split("/")[1];
            Long roleID;
            try {
                roleID = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID format.");
                return;
            }
            List<PermissionResponse> permissions = authorizationService.getAllPermissionsByRoleID(roleID);
            String jsonResponse = gson.toJson(permissions);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, jsonResponse);
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }


    @Override
    @IsAuthenticated
    @HasPermission("AUTHORIZATION_MANAGEMENT")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        Gson gson = new Gson();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 2) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing role ID in the URL.");
            return;
        }
        Long roleID;
        try {
            roleID = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID format in the URL.");
            return;
        }
        PermissionRequest permissionRequest = gson.fromJson(req.getReader(), PermissionRequest.class);
        List<Long> permissionIds = permissionRequest.getPermissionIds();
        try {
            RoleResponse updatedRole = authorizationService.updateAuthority(roleID, permissionIds);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(updatedRole));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}
