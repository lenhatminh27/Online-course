package com.course.service;

import com.course.dto.request.AuthenticationRequest;
import com.course.dto.request.LogoutRequest;
import com.course.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    AuthenticationResponse refreshToken(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    void logout(LogoutRequest logoutRequest);

}


