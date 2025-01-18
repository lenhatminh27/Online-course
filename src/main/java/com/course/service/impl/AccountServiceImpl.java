package com.course.service.impl;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.dto.request.RegisterRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.ErrorResponse;
import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.AccountService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.course.common.utils.PasswordUtils.hashPassword;


public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    private final RoleDAO roleDAO;


    public AccountServiceImpl(AccountDAO accountDAO, RoleDAO roleDAO) {
        this.accountDAO = accountDAO;
        this.roleDAO = roleDAO;
    }


    @Override
    public AccountResponse getCurrentAccount() {
        String emailCurrent = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = this.accountDAO.findByEmail(emailCurrent);
        List<String> roles = accountCurrent.getRoles().stream()
                .map(RoleEntity::getName)
                .map(ERole::name)
                .toList();
        return new AccountResponse(emailCurrent, null, roles);
    }


    @Override
    public void registerAccount(RegisterRequest registerRequest, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        if (accountDAO.existsByEmail(registerRequest.getEmail())) {
            List<String> error = new ArrayList<>();
            error.add("Email already exists");
            ErrorResponse errorResponse = new ErrorResponse(error);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        RoleEntity role = roleDAO.findByName(ERole.LEARNER);
        AccountEntity newAccount = new AccountEntity();
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setPasswordHash(hashPassword(registerRequest.getPassword()));
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setUpdatedAt(LocalDateTime.now());
        newAccount.setRoles(List.of(role));
        accountDAO.save(newAccount);

        AccountProfileEntity accountProfileEntity = new AccountProfileEntity();
        accountProfileEntity.setAccount(newAccount);  // Liên kết AccountProfileEntity với AccountEntity
        accountProfileEntity.setFirstName(registerRequest.getFirstName());
        accountProfileEntity.setLastName(registerRequest.getLastName());
        accountProfileEntity.setCreateAt(LocalDateTime.now());

        // Lưu AccountProfileEntity vào cơ sở dữ liệu
        accountDAO.saveAccountProfile(accountProfileEntity);
    }

}
