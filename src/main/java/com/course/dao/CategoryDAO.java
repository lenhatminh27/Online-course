package com.course.dao;

import com.course.entity.CategoriesEntity;

import java.util.List;

public interface CategoryDAO {
    List<CategoriesEntity> getAllParentCategory();
    CategoriesEntity findById(Long id);

}
