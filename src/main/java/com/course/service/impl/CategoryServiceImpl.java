package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.CategoryDAO;
import com.course.dto.response.CategoryResponse;
import com.course.entity.CategoriesEntity;
import com.course.service.CategoryService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;

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
}
