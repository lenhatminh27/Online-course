package com.course.web.rest;

import com.course.common.utils.MessageUtils;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RefreshTokenDaoImpl;
import com.course.dto.request.AuthenticationRequest;
import com.course.dto.response.AuthenticationResponse;
import com.course.dto.response.ErrorResponse;
import com.course.security.TokenProvider;
import com.course.service.AuthenticationService;
import com.course.service.impl.AuthenticationServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/auth")
public class AuthenticationApi extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final AuthenticationService authenticationService;

    public AuthenticationApi() {
        TokenProvider tokenProvider = new TokenProvider();
        AccountDAO authenticationDAO = new AccountDaoImpl();
        RefreshTokenDAO refreshTokenDAO = new RefreshTokenDaoImpl();
        authenticationService = new AuthenticationServiceImpl(tokenProvider, authenticationDAO, refreshTokenDAO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            AuthenticationRequest authenticationRequest = gson.fromJson(req.getReader(), AuthenticationRequest.class);
            List<String> errors = new ArrayList<>();
            if(ObjectUtils.isEmpty(authenticationRequest)){
                errors.add(MessageUtils.AUTHENTICATION_REQUIRED);
            }
            if(ObjectUtils.isEmpty(authenticationRequest.getEmail())){
                errors.add(MessageUtils.EMAIL_NOT_NULL);
            }
            if(ObjectUtils.isEmpty(authenticationRequest.getPassword())){
                errors.add(MessageUtils.PASSWORD_NOT_NULL);
            }
            if(!ObjectUtils.isEmpty(errors)){
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            AuthenticationResponse authenticate = authenticationService.authenticate(authenticationRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(authenticate));
        }catch (JsonSyntaxException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Invalid JSON format\"}");
        }catch (Exception e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
