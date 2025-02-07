package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.entity.AccountEntity;
import com.course.entity.PermissionEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.ToInstructorService;

import java.util.ArrayList;
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

        RoleEntity instructorRole = roleDAO.findByName(ERole.INSTRUCTOR);
        if (!accountCurrent.getRoles().contains(instructorRole)) {
            List<PermissionEntity> instructorPermissions = instructorRole.getPermissions();

            List<PermissionEntity> existingPermissions = new ArrayList<>();
            List<RoleEntity> roles = accountCurrent.getRoles();

            for (RoleEntity role : roles) {
                List<PermissionEntity> permissions = role.getPermissions();
                for (PermissionEntity permission : permissions) {
                    existingPermissions.add(permission);
                }
            }
            List<PermissionEntity> newPermissions = new ArrayList<>(existingPermissions);

            for (PermissionEntity permission : instructorPermissions) {
                boolean isPermissionAlreadyAssigned = newPermissions.stream()
                        .anyMatch(existingPermission -> existingPermission.getId().equals(permission.getId()));

                if (!isPermissionAlreadyAssigned) {
                    newPermissions.add(permission);
                }
            }
            instructorRole.setPermissions(newPermissions);
            accountCurrent.getRoles().add(instructorRole);
            accountDAO.update(accountCurrent);
        }
    }
}