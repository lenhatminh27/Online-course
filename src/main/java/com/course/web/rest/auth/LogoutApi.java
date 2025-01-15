package com.course.web.rest.auth;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RefreshTokenDaoImpl;
import com.course.dto.request.LogoutRequest;
import com.course.security.TokenProvider;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AuthenticationService;
import com.course.service.impl.AuthenticationServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/logout")
public class LogoutApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AuthenticationService authenticationService;

    public LogoutApi() {
        TokenProvider tokenProvider = new TokenProvider();
        AccountDAO authenticationDAO = new AccountDaoImpl();
        RefreshTokenDAO refreshTokenDAO = new RefreshTokenDaoImpl();
        authenticationService = new AuthenticationServiceImpl(tokenProvider, authenticationDAO, refreshTokenDAO);
    }

    @Override
    @IsAuthenticated
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
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
