package com.course.service;

import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.request.UpdateCategoryRequest;
import com.course.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategoriesParent();
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    boolean isExistCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);
    void deleteCategory(Long categoryId);
    List<CategoryResponse> getAllCategories();
    boolean isDuplicateCategory(UpdateCategoryRequest updateCategoryRequest);
}
    