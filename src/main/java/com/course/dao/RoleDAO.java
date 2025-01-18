package com.course.dao;

import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;

import java.util.List;

public interface RoleDAO {

    boolean existsById(Long id);

    List<RoleEntity> findAll();

    RoleEntity update(RoleEntity role);

    RoleEntity findById(Long id);

    RoleEntity findByName(ERole name);

    List<RoleEntity> findByNameLike(List<String> names);
}
