package com.course.service;

import com.course.dto.request.RegisterRequest;
import com.course.dto.response.AccountResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AccountService {

    AccountResponse getCurrentAccount();
    void registerAccount(RegisterRequest registerRequest, HttpServletResponse response) throws IOException  ;
}
