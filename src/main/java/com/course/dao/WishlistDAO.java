package com.course.dao;

import com.course.entity.AccountEntity;
import com.course.entity.WishlistEntity;

import java.util.List;

public interface WishlistDAO {


    WishlistEntity save(WishlistEntity wishlist);

    void delete(WishlistEntity wishlist);

    boolean checkCourseExistInWishlist(Long courseId, Long accountId);

    WishlistEntity findWishlistById(Long id);

    List<WishlistEntity> getByUserId(Long id);
}
