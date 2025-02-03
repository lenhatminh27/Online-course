package com.course.service;

import com.course.dto.request.AccountProfileRequest;
import com.course.dto.response.AccountProfileResponse;

public interface AccountProfileService {
    AccountProfileResponse getAccountProfileByAccount();

    AccountProfileResponse updateAccountProfile(AccountProfileRequest body);
}
