package com.course.dao;

import com.course.entity.RatingEntity;

import java.util.List;

public interface RatingDAO {
    Double calRatingByCourseId(Long courseId);

    List<RatingEntity> getAllRating();

    List<RatingEntity> getRatingByCourseId(long courseId);

    RatingEntity createRating(RatingEntity rating);

    RatingEntity updateRating(RatingEntity rating);

    void deleteRatingByRatingId(long ratingId);

    RatingEntity getRatingByRatingId(long ratingId);

    boolean isExistRating(long courseId, long accountId);

    List<RatingEntity> getTop3Rating(long courseId);

    List<RatingEntity> findByReviewAndRatingAndCourseId(String review, int rating, Long courseId);
}
