package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dto.response.AccountResponse;
import com.course.entity.AccountEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.AccountService;

import java.util.List;


public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
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
}
