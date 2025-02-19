package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.CategoryDAO;
import com.course.dao.CourseDAO;
import com.course.dto.request.CreateCourseRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.CourseResponse;
import com.course.entity.AccountEntity;
import com.course.entity.CategoriesEntity;
import com.course.entity.CourseEntity;
import com.course.entity.enums.CourseStatus;
import com.course.exceptions.NotFoundException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.CourseService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CategoryDAO categoryDAO;

    private final CourseDAO courseDAO;

    private final AccountDAO accountDAO;

    @Override
    public CourseResponse createCourse(CreateCourseRequest createCourseRequest) {
        AccountEntity account = getAuthenticatedAccount();
        CategoriesEntity categories = categoryDAO.findById(createCourseRequest.getCategoriesId());
        if(ObjectUtils.isEmpty(categories)) {
            throw new NotFoundException("Không tìm thấy category");
        }
        CourseEntity course = CourseEntity.builder()
                .title(createCourseRequest.getTitle())
                .categories(List.of(categories))
                .createdBy(account.getEmail())
                .accountCreated(account)
                .status(CourseStatus.DRAFT)
                .price(BigDecimal.ZERO)
                .build();
        CourseEntity newCourse = courseDAO.createCourse(course);
        return convertToCourseResponse(newCourse);
    }

    private AccountEntity getAuthenticatedAccount() {
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }

    private CourseResponse convertToCourseResponse(CourseEntity courseEntity) {
        AccountEntity author = courseEntity.getAccountCreated();
        AccountResponse accountResponse = new AccountResponse(author.getEmail(), author.getAvatar());
        List<CategoryResponse> categories = courseEntity.getCategories().stream().map(this::convertToCategoryResponse).toList();
        return CourseResponse.builder()
                .id(courseEntity.getId())
                .title(courseEntity.getTitle())
                .description(courseEntity.getDescription())
                .createdAt(courseEntity.getCreatedAt())
                .updatedAt(courseEntity.getUpdatedAt())
                .status(courseEntity.getStatus())
                .price(courseEntity.getPrice())
                .thumbnail(courseEntity.getThumbnail())
                .categories(categories)
                .accountResponse(accountResponse)
                .build();
    }

    private CategoryResponse convertToCategoryResponse(CategoriesEntity categoriesEntity) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoriesEntity.getId());
        categoryResponse.setName(categoriesEntity.getName());
        categoryResponse.setDescription(categoriesEntity.getDescription());
        categoryResponse.setCreatedAt(categoriesEntity.getCreatedAt());
        categoryResponse.setUpdatedAt(categoriesEntity.getUpdatedAt());
        return categoryResponse;
    }



}
