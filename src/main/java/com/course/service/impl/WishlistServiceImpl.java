package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.*;
import com.course.dto.request.WishlistCourseRequest;
import com.course.dto.response.TestWishlistRespone;
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
    private final RatingDao ratingDao;


    @Override
    public void createWishlist(Long courseId) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);


        CourseEntity course = courseDAO.findById(courseId);

        if (course == null) {
            throw new NotFoundException("khóa học không tồn tại");
        }

        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setAccount(accountCurrent);
        wishlist.setCourse(course);
        wishlistDAO.save(wishlist);
    }

    @Override
    public void deleteWishlist(Long courseId) {
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());
        WishlistEntity wishlist = wishlistDAO.findWishlistByCourseIdAndAccountId(courseId, account.getId());

        if (wishlist == null) {
            throw new ForbiddenException("khóa học này không tồn tại trong danh sách yêu thích");
        }

        if (!AuthenticationContextHolder.getContext().getAuthorities().contains(AuthoritiesConstants.ROLE_ADMIN) && !account.getId().equals(wishlist.getAccount().getId())) {
            throw new ForbiddenException("Bạn không có quyền xóa khóa học này khỏi wishlist");
        }

        wishlistDAO.delete(wishlist);

    }

    @Override
    @Transactional
    //@Transactional giúp duy trì session Hibernate suốt quá trình truy vấn, tránh lỗi LazyInitializationException
    public List<WishlistCourseRespone> getWishlist() {
        // Lấy thông tin người dùng hiện tại từ email
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());

        // Lấy danh sách các mục trong wishlist của người dùng
        List<WishlistEntity> wishlists = wishlistDAO.getByUserId(account.getId());

        // Định dạng ngày giờ thành chuỗi (nếu cần thiết)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Chuyển đổi danh sách WishlistEntity sang WishlistCourseRespone
        return wishlists.stream().map(wishlist -> {
            CourseEntity course = wishlist.getCourse();

            // Gọi hàm tính rating trung bình cho khóa học dựa trên courseId
            Double rating = ratingDao.calRatingByCourseId(course.getId());

            // Trả về đối tượng TestWishlistRespone với dữ liệu của khóa học và điểm rating
            return new WishlistCourseRespone(
                    course.getId(),                             // courseId
                    course.getTitle(),                          // title
                    course.getCreatedBy(),                      // createBy (email của người tạo khóa học)
                    course.getDescription(),                    // description
                    course.getCreatedAt().format(formatter),    // createdAt
                    course.getPrice(),                          // price
                    rating,                                      // rating tính từ calRatingByCourseId
                    course.getThumbnail()
            );
        }).collect(Collectors.toList());
    }


}



