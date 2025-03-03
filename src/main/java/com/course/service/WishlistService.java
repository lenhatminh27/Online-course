package com.course.service;

import com.course.dto.request.WishlistCourseRequest;
import com.course.dto.response.TestWishlistRespone;
import com.course.dto.response.WishlistCourseRespone;

import java.util.List;

public interface WishlistService {
    void createWishlist(Long CourseId);
    void deleteWishlist(Long courseId);
    List<WishlistCourseRespone> getWishlist();
}
