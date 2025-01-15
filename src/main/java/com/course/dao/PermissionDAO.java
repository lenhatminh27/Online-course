package com.course.dao;

import com.course.entity.PermissionEntity;

import java.util.List;

public interface PermissionDAO {
    List<PermissionEntity> getAllPermission();

    List<Long> getPermissionByRoleID(Long roleID);

    List<PermissionEntity> findAllByIds(List<Long> permissionIDs);
}
