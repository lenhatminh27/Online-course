package com.course.web.rest.auth;

import com.course.common.utils.MessageUtils;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.AuthenticationRequest;
import com.course.dto.response.AuthenticationResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.AuthenticationException;
import com.course.service.AuthenticationService;
import com.course.service.impl.AuthenticationServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/auth")
public class AuthenticationApi extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AuthenticationService authenticationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        authenticationService = getBean(AuthenticationServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
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
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Invalid JSON format\"}");
        }catch (AuthenticationException authenticationException){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, gson.toJson(authenticationException.getMessage()));
        }
        catch (Exception e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
