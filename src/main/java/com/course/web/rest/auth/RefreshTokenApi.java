package com.course.web.rest.auth;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.AuthenticationResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AuthenticationService;
import com.course.service.impl.AuthenticationServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/refresh-token")
public class RefreshTokenApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AuthenticationService authenticationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        authenticationService = getBean(AuthenticationServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @HasPermission("REFRESH_TOKEN")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            AuthenticationResponse authenticate = this.authenticationService.refreshToken(req, resp);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(authenticate));
        }catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
