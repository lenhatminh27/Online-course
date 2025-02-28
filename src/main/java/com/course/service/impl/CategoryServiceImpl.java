package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.CategoryDAO;
import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.request.UpdateCategoryRequest;
import com.course.dto.response.BookmarksBlogResponse;
import com.course.dto.response.CategoryResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.CategoriesEntity;
import com.course.exceptions.NotFoundException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;
    private final AccountDAO accountDAO;

    @Override
    public List<CategoryResponse> getAllCategoriesParent() {
        List<CategoriesEntity> res = categoryDAO.getAllParentCategory();
        return res.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private CategoryResponse convertToResponse(CategoriesEntity categoriesEntity) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoriesEntity.getId());
        categoryResponse.setName(categoriesEntity.getName());
        categoryResponse.setDescription(categoriesEntity.getDescription());
        categoryResponse.setCreatedAt(categoriesEntity.getCreatedAt());
        categoryResponse.setUpdatedAt(categoriesEntity.getUpdatedAt());
        return categoryResponse;
    }




    @Override
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);
        if (!categoryDAO.existCategory(categoryCreateRequest.getName().trim())) {
            CategoriesEntity parentCategory = null;
            if (categoryCreateRequest.getParentCategoryId() != null) {
                parentCategory = categoryDAO.findById(categoryCreateRequest.getParentCategoryId());
            }
            CategoriesEntity categoryEntity = new CategoriesEntity();
            categoryEntity.setName(categoryCreateRequest.getName().trim());
            categoryEntity.setCreatedAt(LocalDateTime.now());
            categoryEntity.setUpdatedAt(LocalDateTime.now());
            categoryEntity.setDescription(categoryCreateRequest.getDescription());
            categoryEntity.setParentCategories(parentCategory);
            CategoriesEntity categorySave = categoryDAO.createCategory(categoryEntity);
            return convertToResponse(categorySave);
        }
        return null;
    }

    @Override
    public boolean isExistCategory(CategoryCreateRequest categoryCreateRequest) {
        return categoryDAO.existCategory(categoryCreateRequest.getName().trim());
    }

    @Override
    public boolean isDuplicateCategory(UpdateCategoryRequest updateCategoryRequest) {
        return categoryDAO.isDuplicateCategoryName(updateCategoryRequest.getCategoryId(), updateCategoryRequest.getName().trim());
    }

    @Override
    public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        CategoriesEntity categoryCurrent = categoryDAO.findById(updateCategoryRequest.getCategoryId());
        if (!ObjectUtils.isEmpty(updateCategoryRequest.getDescription())) {
            categoryCurrent.setDescription(updateCategoryRequest.getDescription().trim());
        }
        if (!ObjectUtils.isEmpty(updateCategoryRequest.getParentCategoryId())) {
            categoryCurrent.setParentCategories(categoryDAO.findById(updateCategoryRequest.getParentCategoryId()));
        }
        if (!ObjectUtils.isEmpty(updateCategoryRequest.getName().trim())) {
            categoryCurrent.setName(updateCategoryRequest.getName().trim());
        }
        categoryDAO.updateCategory(categoryCurrent);
        return convertToResponse(categoryCurrent);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        CategoriesEntity category = categoryDAO.findById(categoryId);
        if (category != null) {
            categoryDAO.deleteCategory(category);
        }
        else {
            throw new NotFoundException("Category không tồn tại");
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoriesEntity> categories = categoryDAO.getAllCategories();
        return categories.stream().map(category -> {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            response.setDescription(category.getDescription());
            response.setCreatedAt(category.getCreatedAt());
            response.setUpdatedAt(category.getUpdatedAt());
            return response;
        }).collect(Collectors.toList());
    }
}
