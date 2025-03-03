package com.course.service;

import com.course.core.repository.data.PageRequest;
import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.request.CategoryFilterRequest;
import com.course.dto.request.UpdateCategoryRequest;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategoriesParent();
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    boolean isExistCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);
    void deleteCategory(Long categoryId);
    List<CategoryResponse> getAllCategories();
    boolean isDuplicateCategory(UpdateCategoryRequest updateCategoryRequest);
    PageResponse<CategoryResponse> getCategories(CategoryFilterRequest categoryFilterRequest);
}
    