package com.course.web.rest.auth;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RefreshTokenDaoImpl;
import com.course.dto.response.AuthenticationResponse;
import com.course.security.TokenProvider;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AuthenticationService;
import com.course.service.impl.AuthenticationServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/refresh-token")
public class RefreshTokenApi extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final AuthenticationService authenticationService;

    public RefreshTokenApi() {
        TokenProvider tokenProvider = new TokenProvider();
        AccountDAO authenticationDAO = new AccountDaoImpl();
        RefreshTokenDAO refreshTokenDAO = new RefreshTokenDaoImpl();
        authenticationService = new AuthenticationServiceImpl(tokenProvider, authenticationDAO, refreshTokenDAO);
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
