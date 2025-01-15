package com.course.dao;

import com.course.entity.RoleEntity;

import java.util.List;

public interface RoleDAO {

    boolean existsById(Long id);

    List<RoleEntity> findAll();

    RoleEntity update(RoleEntity role);

    RoleEntity findById(Long id);
}
