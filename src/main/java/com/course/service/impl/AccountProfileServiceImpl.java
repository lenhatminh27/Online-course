package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.AccountProfileDAO;
import com.course.dto.request.AccountProfileRequest;
import com.course.dto.response.AccountProfileResponse;
import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.AccountProfileService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class AccountProfileServiceImpl implements AccountProfileService {
    private final AccountProfileDAO accountProfileDAO;
    private final AccountDAO accountDAO;

    @Override
    public AccountProfileResponse getAccountProfileByAccount() {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);
        AccountProfileEntity accountProfileEntity = accountCurrent.getAccountProfile();
        AccountProfileResponse accountProfileResponse = new AccountProfileResponse();
        accountProfileResponse.setFirstName(accountProfileEntity.getFirstName());
        accountProfileResponse.setLastName(accountProfileEntity.getLastName());
        accountProfileResponse.setId(accountProfileEntity.getId());
        accountProfileResponse.setAddress(accountProfileEntity.getAddress());
        accountProfileResponse.setPhoneNumber(accountProfileEntity.getPhoneNumber());
        if (!ObjectUtils.isEmpty(accountProfileEntity.getDateOfBirth())){
            accountProfileResponse.setDateOfBirth(accountProfileEntity.getDateOfBirth().toString());
        }
        return accountProfileResponse;
    }

    public AccountProfileResponse updateAccountProfile(AccountProfileRequest accountProfileRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);

        AccountProfileEntity accountProfileEntity = accountCurrent.getAccountProfile();

        // Cập nhật các trường từ request
        if (!ObjectUtils.isEmpty(accountProfileRequest.getFirstName())) {
            accountProfileEntity.setFirstName(accountProfileRequest.getFirstName());
        }
        if (!ObjectUtils.isEmpty(accountProfileRequest.getLastName())) {
            accountProfileEntity.setLastName(accountProfileRequest.getLastName());
        }
        if (!ObjectUtils.isEmpty(accountProfileRequest.getPhoneNumber())) {
            accountProfileEntity.setPhoneNumber(accountProfileRequest.getPhoneNumber());
        }
        if (!ObjectUtils.isEmpty(accountProfileRequest.getAddress())) {
            accountProfileEntity.setAddress(accountProfileRequest.getAddress());
        }
        if (!ObjectUtils.isEmpty(accountProfileRequest.getAvatar())) {
            accountProfileEntity.setAvatar(accountProfileRequest.getAvatar());
        }
        String dateOfBirth = accountProfileRequest.getDateOfBirth();
        if (!ObjectUtils.isEmpty(dateOfBirth)) {
            LocalDate localDate = LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            accountProfileEntity.setDateOfBirth(localDateTime);
        }
        // Lưu thay đổi vào database
        accountProfileDAO.updateAccountProfile(accountProfileEntity);

        // Trả về phản hồi với thông tin hồ sơ đã cập nhật
        AccountProfileResponse accountProfileResponse = new AccountProfileResponse();
        accountProfileResponse.setFirstName(accountProfileEntity.getFirstName());
        accountProfileResponse.setLastName(accountProfileEntity.getLastName());
        accountProfileResponse.setId(accountProfileEntity.getId());
        accountProfileResponse.setAddress(accountProfileEntity.getAddress());
        accountProfileResponse.setAvatar(accountProfileEntity.getAvatar());
        accountProfileResponse.setPhoneNumber(accountProfileEntity.getPhoneNumber());
        accountProfileResponse.setDateOfBirth(accountProfileEntity.getDateOfBirth().toString());
        return accountProfileResponse;
    }
}
