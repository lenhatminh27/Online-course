package com.course.service;

import com.course.dto.response.PermissionResponse;
import com.course.dto.response.RoleResponse;

import java.util.List;

public interface AuthorizationService {
    List<PermissionResponse> getAllPermissionsByRoleID(Long roleID);

    List<RoleResponse> getAllRole();

    RoleResponse updateAuthority(Long roleID, List<Long> permissionIds);
}
