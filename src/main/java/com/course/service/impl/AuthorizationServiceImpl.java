package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.PermissionDAO;
import com.course.dao.RoleDAO;
import com.course.dto.response.PermissionResponse;
import com.course.dto.response.RoleResponse;
import com.course.entity.PermissionEntity;
import com.course.entity.RoleEntity;
import com.course.exceptions.NotFoundException;
import com.course.service.AuthorizationService;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final PermissionDAO permissionDAO;

    private final RoleDAO roleDAO;



    @Override
    public List<PermissionResponse> getAllPermissionsByRoleID(Long roleID) {
        if(!roleDAO.existsById(roleID)){
            throw new NotFoundException("Role id not found");
        }
        Set<Long> assignedPermissionIds = new HashSet<>(permissionDAO.getPermissionByRoleID(roleID));
        return permissionDAO.getAllPermission().stream()
                .map(permission -> new PermissionResponse(
                        permission.getId(),
                        permission.getName().name(),
                        assignedPermissionIds.contains(permission.getId())
                ))
                .toList();
    }

    @Override
    public List<RoleResponse> getAllRole() {
        return roleDAO.findAll().stream().map(role -> new RoleResponse(
                role.getId(),
                role.getName().name()
        )).toList();
    }

    @Override
    public RoleResponse updateAuthority(Long roleID, List<Long> permissionIds) {
        if(!roleDAO.existsById(roleID)){
            throw new NotFoundException("Role id not found");
        }
        RoleEntity roleEntity = roleDAO.findById(roleID);
        List<PermissionEntity> permissionEntities = permissionDAO.findAllByIds(permissionIds);
        roleEntity.setPermissions(permissionEntities);
        RoleEntity role = roleDAO.update(roleEntity);
        return new RoleResponse(role.getId(), role.getName().name());
    }
}
