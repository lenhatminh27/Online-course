package com.course.service;

import com.course.dto.request.FilterRatingRequest;
import com.course.dto.request.RatingCreateRequest;
import com.course.dto.request.RatingUpdateRequest;
import com.course.dto.response.RatingResponse;

import java.util.List;

public interface RatingService {
    List<RatingResponse> findRatingByCourse(Long courseId);

    RatingResponse createRating(RatingCreateRequest ratingCreateRequest);

    RatingResponse updateRating(Long id, RatingUpdateRequest ratingUpdateRequest);

    void deleteRating(Long ratingId);

    List<RatingResponse> getTop3Rating(long courseId);

    List<RatingResponse> getRatingByReviewAndRatingAndCourseId(FilterRatingRequest filterRatingRequest);
}
