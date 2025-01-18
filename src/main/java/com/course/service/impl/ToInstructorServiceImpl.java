package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.entity.AccountEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.ToInstructorService;

import java.util.List;

public class ToInstructorServiceImpl implements ToInstructorService {
    private final AccountDAO accountDAO;

    private final RoleDAO roleDAO;

    public ToInstructorServiceImpl(AccountDAO accountDAO, RoleDAO roleDAO) {
        this.accountDAO = accountDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public void updateRole() {
        String emailCurrent = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = this.accountDAO.findByEmail(emailCurrent);
        List<RoleEntity> currentRole = accountCurrent.getRoles();
        currentRole.add(roleDAO.findByName(ERole.INSTRUCTOR));
        accountCurrent.setRoles(currentRole);
        accountDAO.update(accountCurrent);
    }
}
