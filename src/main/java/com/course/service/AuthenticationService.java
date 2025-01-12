package com.course.service;

import com.course.dto.request.AuthenticationRequest;
import com.course.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
