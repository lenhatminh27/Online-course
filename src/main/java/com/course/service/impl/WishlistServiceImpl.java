package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.*;
import com.course.dto.request.WishlistCourseRequest;
import com.course.dto.response.WishlistCourseRespone;
import com.course.entity.AccountEntity;
import com.course.entity.CourseEntity;
import com.course.entity.WishlistEntity;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.WishlistService;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final AccountDAO accountDAO;
    private final WishlistDAO wishlistDAO;
    private final CourseDAO courseDAO;
    

    @Override
    public void createWishlist(WishlistCourseRequest wishlistCourseRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);


        CourseEntity course = courseDAO.findById(wishlistCourseRequest.getCourseId());

        if (course == null) {
            throw new NotFoundException("khóa học không tồn tại");
        }

        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setAccount(accountCurrent);
        wishlist.setCourse(course);
        wishlistDAO.save(wishlist);
    }

    @Override
    public void deleteWishlist(Long wishlistId) {
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());
        WishlistEntity wishlist = wishlistDAO.findWishlistById(wishlistId);

        if (wishlist == null) {
            throw new ForbiddenException("khóa học này không tồn tại trong danh sách yêu thích");
        }

        if (!AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN) && !account.getId().equals(wishlist.getAccount().getId())) {
            throw new ForbiddenException("Bạn không có quyền xóa khóa học này khỏi wishlist");
        }

        wishlistDAO.delete(wishlist);

    }

    @Override
    @Transactional //@Transactional giúp duy trì session Hibernate suốt quá trình truy vấn, tránh lỗi LazyInitializationException
    public List<WishlistCourseRespone> getWishlist() {
        // Lấy thông tin người dùng hiện tại từ email
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());

        // Lấy danh sách các mục trong wishlist của người dùng
        List<WishlistEntity> wishlists = wishlistDAO.getByUserId(account.getId());

        // Kiểm tra nếu không có khóa học nào trong wishlist
        if (wishlists == null || wishlists.isEmpty()) {
            throw new ForbiddenException("Bạn chưa thêm khóa học vào danh sách yêu thích");
        }

        // Định dạng ngày giờ thành chuỗi (nếu cần thiết)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Chuyển đổi danh sách WishlistEntity sang WishlistCourseRespone
        return wishlists.stream().map(wishlist -> {
            CourseEntity course = wishlist.getCourse();
            return new WishlistCourseRespone(
                    course.getId(),                         // courseId
                    course.getTitle(),                      // title
                    course.getCreatedBy(),         // createBy (email của người tạo khóa học)
                    course.getDescription(),                // description
                    course.getCreatedAt().format(formatter), // createAt
                    course.getPrice()                       // price
            );
        }).collect(Collectors.toList());
    }

}
