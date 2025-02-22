package com.course.service;

import com.course.dto.request.WishlistCourseRequest;
import com.course.dto.response.WishlistCourseRespone;

import java.util.List;

public interface WishlistService {
    void createWishlist(WishlistCourseRequest wishlistCourseRequest);
    void deleteWishlist(Long wishlistId);
    List<WishlistCourseRespone> getWishlist();
}
