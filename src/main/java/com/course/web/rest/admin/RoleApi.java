package com.course.web.rest.admin;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.RoleResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AuthorizationService;
import com.course.service.impl.AuthorizationServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/roles")
public class RoleApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AuthorizationService authorizationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.authorizationService = getBean(AuthorizationServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("ADMIN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<RoleResponse> roles = this.authorizationService.getAllRole();
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(roles));
    }
}
