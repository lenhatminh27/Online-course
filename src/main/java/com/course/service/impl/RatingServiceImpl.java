package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.CourseDAO;
import com.course.dao.EnrollmentDAO;
import com.course.dao.RatingDAO;
import com.course.dto.request.FilterRatingRequest;
import com.course.dto.request.RatingCreateRequest;
import com.course.dto.request.RatingUpdateRequest;
import com.course.dto.response.*;
import com.course.entity.*;
import com.course.exceptions.BadRequestException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.exceptions.RatingException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.CourseService;
import com.course.service.RatingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingDAO ratingDAO;
    private final AccountDAO accountDAO;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;

    @Override
    public List<RatingResponse> findRatingByCourse(Long courseId) {
        List<RatingEntity> ratingEntities = ratingDAO.getRatingByCourseId(courseId);
        return ratingEntities.stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RatingResponse createRating(RatingCreateRequest ratingCreateRequest) {

        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);

        CourseEntity courseEntity = courseDAO.findById(ratingCreateRequest.getCourseId());
        if (ObjectUtils.isEmpty(courseEntity)) {
            throw new NotFoundException("Khóa học không tồn tại");
        }

        boolean enrollment = enrollmentDAO.getEnrollmentByAccountIdAndCourseId(accountEntity.getId(), courseEntity.getId());
        if (courseEntity.getCreatedBy().equals(email) || AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN)) {
            enrollment = true;
        }
        if (!enrollment) {
            throw new ForbiddenException("Bạn phải đăng kí khóa học để có thể đánh giá");
        }

        if (ratingDAO.isExistRating(ratingCreateRequest.getCourseId(), accountEntity.getId())) {
            throw new RatingException("Bạn chỉ có thể đánh giá 1 lần");
        }

        if (ratingCreateRequest.getRating() > 5 || ratingCreateRequest.getRating() < 0) {
            throw new RatingException("Bạn chỉ có thể đánh giá từ 1 tới 5");
        }


        // Tạo và lưu thông tin rating
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setAccount(accountEntity);
        ratingEntity.setCourse(courseEntity);
        ratingEntity.setRating(ratingCreateRequest.getRating());
        ratingEntity.setReview(ratingCreateRequest.getReview());
        ratingEntity.setCreatedAt(LocalDateTime.now());
        ratingEntity.setUpdatedAt(LocalDateTime.now());

        RatingEntity savedRating = ratingDAO.createRating(ratingEntity);
        return convertToRatingResponse(savedRating);
    }


    @Override
    public RatingResponse updateRating(Long ratingId, RatingUpdateRequest ratingUpdateRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);
        // Fetch the existing rating
        RatingEntity ratingEntity = ratingDAO.getRatingByRatingId(ratingId);
        if (ratingEntity == null) {
            throw new NotFoundException("Đánh giá không tồn tại");
        }

        if (!accountEntity.getId().equals(ratingEntity.getAccount().getId())
                && !AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN)) {
            throw new RatingException("Bạn không có quyền thay đổi đánh giá");
        }

        ratingEntity.setRating(ratingUpdateRequest.getRating());
        ratingEntity.setReview(ratingUpdateRequest.getReview());
        ratingEntity.setUpdatedAt(LocalDateTime.now());

        ratingDAO.updateRating(ratingEntity);

        return convertToRatingResponse(ratingEntity);
    }

    @Override
    public void deleteRating(Long ratingId) {
        // Get the authenticated user's email
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);

        // Fetch the rating to be deleted
        RatingEntity ratingEntity = ratingDAO.getRatingByRatingId(ratingId);
        if (ratingEntity == null) {
            throw new NotFoundException("Đánh giá không tồn tại");
        }

        if (!accountEntity.getId().equals(ratingEntity.getAccount().getId())
                && !AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN)) {
            throw new RatingException("Bạn không có quyền xóa đánh giá");
        }

        ratingDAO.deleteRatingByRatingId(ratingId);
    }

    @Override
    public List<RatingResponse> getTop3Rating(long courseId) {
        List<RatingEntity> ratingEntities = ratingDAO.getTop3Rating(courseId);
        return ratingEntities.stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingByReviewAndRatingAndCourseId(FilterRatingRequest filterRatingRequest) {
        // Lấy các tham số cần lọc từ request
        String review = filterRatingRequest.getReview();
        int rating = filterRatingRequest.getRating();
        Long courseId = filterRatingRequest.getCourseId();
        List<RatingEntity> ratingEntities = ratingDAO.findByReviewAndRatingAndCourseId(review, rating, courseId);

        return ratingEntities.stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }


    private RatingResponse convertToRatingResponse(RatingEntity ratingEntity) {

        AccountResponse accountResponse = convertToAccountResponse(ratingEntity.getAccount());
        CourseResponse courseResponse = convertToCourseResponse(ratingEntity.getCourse());
        return new RatingResponse(
                ratingEntity.getId(),
                ratingEntity.getCreatedAt(),
                ratingEntity.getRating(),
                ratingEntity.getReview(),
                accountResponse,
                courseResponse,
                ratingEntity.getUpdatedAt()
        );
    }

    private AccountResponse convertToAccountResponse(AccountEntity accountEntity) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail(accountEntity.getEmail());
        accountResponse.setAvatar(accountEntity.getAvatar());
        return accountResponse;
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
