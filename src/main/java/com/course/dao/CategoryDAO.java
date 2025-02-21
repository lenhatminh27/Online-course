package com.course.dao;

import com.course.entity.CategoriesEntity;

import java.util.List;

public interface CategoryDAO {
    List<CategoriesEntity> getAllParentCategory();
    CategoriesEntity findById(Long id);
    CategoriesEntity createCategory(CategoriesEntity category);
    boolean existCategory(String categoryName);
    List<CategoriesEntity> findAllChildrenCategories(Long id);
    List<CategoriesEntity> getAllCategories();
}
