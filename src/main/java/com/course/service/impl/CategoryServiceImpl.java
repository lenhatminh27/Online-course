package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.CategoryDAO;
import com.course.dao.SearchHistoryDAO;
import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.request.CategoryFilterRequest;
import com.course.dto.request.UpdateCategoryRequest;
import com.course.dto.response.BookmarksBlogResponse;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.PageResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.CategoriesEntity;
import com.course.entity.SearchHistoryEntity;
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
    private final SearchHistoryDAO searchHistoryDAO;

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

    private AccountEntity getAuthenticatedAccount() {
        if (AuthenticationContextHolder.getContext() == null) {
            return null;
        }
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }

    @Override
    public PageResponse<CategoryResponse> getCategories(CategoryFilterRequest filterRequest) {
        AccountEntity account = getAuthenticatedAccount();

        // Lưu lại lịch sử tìm kiếm nếu có search và người dùng đã xác thực
        if (!ObjectUtils.isEmpty(account) && !ObjectUtils.isEmpty(filterRequest.getSearch())) {
            SearchHistoryEntity searchHistory = new SearchHistoryEntity();
            searchHistory.setAccount(account);
            searchHistory.setContent(filterRequest.getSearch());
            searchHistory.setCreatedAt(LocalDateTime.now());
            searchHistoryDAO.save(searchHistory);
        }

        // Gọi DAO để lấy danh sách category theo bộ lọc
        PageResponse<CategoriesEntity> pageResponse = categoryDAO.getCategoriesByPage(filterRequest);

        // Convert từ entity sang response
        List<CategoryResponse> categories = pageResponse.getData().stream()
                .map(this::convertToResponse)
                .toList();

        // Trả về PageResponse với danh sách category đã convert
        return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalPages(), categories);
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
            throw new NotFoundException("Thể loại không tồn tại");
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
