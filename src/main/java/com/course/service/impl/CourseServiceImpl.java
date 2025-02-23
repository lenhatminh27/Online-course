package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.CategoryDAO;
import com.course.dao.CourseDAO;
import com.course.dto.request.CourseInstructorFilterRequest;
import com.course.dto.request.CreateCourseRequest;
import com.course.dto.request.UpdateCourseRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.CourseResponse;
import com.course.dto.response.PageResponse;
import com.course.entity.AccountEntity;
import com.course.entity.CategoriesEntity;
import com.course.entity.CourseEntity;
import com.course.entity.enums.CourseStatus;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
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
    public PageResponse<CourseResponse> getAllListCourseByUserCurrent(CourseInstructorFilterRequest filterRequest) {
        AccountEntity accountCurrent = getAuthenticatedAccount();
        PageResponse<CourseEntity> pageResponse = courseDAO.findByAccountCreatedId(accountCurrent.getId(), filterRequest);
        List<CourseResponse> courses = pageResponse.getData().stream()
                .map(this::convertToCourseResponse)
                .toList();

        return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalPages(), courses);
    }

    @Override
    public CourseResponse updateCourse(UpdateCourseRequest request) {
        CourseEntity courseUpdated = courseDAO.findById(request.getCourseId());
        if (ObjectUtils.isEmpty(courseUpdated)) {
            throw new NotFoundException("Không tìm thấy khóa học");
        }
        String accountCurrent = getAuthenticatedAccountEmail();
        boolean isAdmin = AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN);
        boolean isOwner = courseUpdated.getAccountCreated().getEmail().equals(accountCurrent);
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("Không có quyền thay đổi");
        }
        if(!ObjectUtils.isEmpty(request.getTitle())){
            courseUpdated.setTitle(request.getTitle());
        }
        if(!ObjectUtils.isEmpty(request.getDescription())){
            courseUpdated.setDescription(request.getDescription());
        }
        if(!ObjectUtils.isEmpty(request.getPrice())){
            courseUpdated.setPrice(request.getPrice());
        }
        if(!ObjectUtils.isEmpty(request.getThumbnail())){
            courseUpdated.setThumbnail(request.getThumbnail());
        }
        if(!ObjectUtils.isEmpty(request.getStatus())){
            courseUpdated.setStatus(request.getStatus());
        }
        if(!ObjectUtils.isEmpty(request.getCategoriesId())){
            CategoriesEntity categories = categoryDAO.findById(request.getCategoriesId());
            if(!ObjectUtils.isEmpty(categories)){
                courseUpdated.setCategories(List.of(categories));
            }
        }
        CourseEntity course = courseDAO.updateCourse(courseUpdated);
        return convertToCourseResponse(course);
    }

    private String getAuthenticatedAccountEmail() {
        return AuthenticationContextHolder.getContext().getEmail();
    }

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

    @Override
    public CourseResponse findById(Long id) {
        CourseEntity course = courseDAO.findById(id);
        if(ObjectUtils.isEmpty(course)) {
            throw new NotFoundException("Không tìm thấy khóa học");
        }
        return convertToCourseResponse(course);
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
