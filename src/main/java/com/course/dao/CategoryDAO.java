package com.course.dao;

import com.course.dto.request.CategoryFilterRequest;
import com.course.dto.response.PageResponse;
import com.course.entity.CategoriesEntity;

import java.awt.print.Pageable;
import java.util.List;

public interface CategoryDAO {
    List<CategoriesEntity> getAllParentCategory();
    CategoriesEntity findById(Long id);
    CategoriesEntity createCategory(CategoriesEntity category);
    boolean existCategory(String categoryName);
    List<CategoriesEntity> findAllChildrenCategories(Long id);
    List<CategoriesEntity> getAllCategories();
    CategoriesEntity findByName(String name);
    PageResponse<CategoriesEntity> getCategoriesByPage(CategoryFilterRequest categoryFilter);
    CategoriesEntity updateCategory(CategoriesEntity category);
    void deleteCategory(CategoriesEntity category);
    boolean isDuplicateCategoryName(Long categoryId, String categoryName);
}
