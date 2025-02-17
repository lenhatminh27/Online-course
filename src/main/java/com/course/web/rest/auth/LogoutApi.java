package com.course.web.rest.auth;

import com.course.common.utils.ResponseUtils;
import com.course.dto.request.LogoutRequest;
import com.course.security.annotations.IsAuthenticated;
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

@WebServlet("/api/logout")
public class LogoutApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AuthenticationService authenticationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        authenticationService = getBean(AuthenticationServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            LogoutRequest logoutRequest = gson.fromJson(req.getReader(), LogoutRequest.class);
            this.authenticationService.logout(logoutRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("oke"));
        }catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("oke"));
        }
    }
}
