package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.WishlistCourseRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.response.WishlistCourseRespone;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.WishlistService;
import com.course.service.impl.WishlistServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/wishlist/*")
public class WishlistApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private WishlistService wishlistService;

    public void init(ServletConfig config) throws ServletException {
        wishlistService = getBean(WishlistServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    @HasPermission("WISHLIST_COURSE")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Gọi service để lấy danh sách khóa học đã thêm vào danh sách yêu thích
            List<WishlistCourseRespone> wishlistCourseRespones = wishlistService.getWishlist();

            // Kiểm tra nếu danh sách rỗng
            if (ObjectUtils.isEmpty(wishlistCourseRespones)) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("Không có khóa học nào ở trong danh sách yêu thích của bạn"));
                return;
            }

            // Ghi phản hồi JSON về client
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(wishlistCourseRespones));
        } catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }

    @Override
    @IsAuthenticated
    @HasPermission("WISHLIST_COURSE")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        WishlistCourseRequest wishlistCourseRequest = gson.fromJson(req.getReader(), WishlistCourseRequest.class);
        List<String> errors = new ArrayList<>();

        if (ObjectUtils.isEmpty(wishlistCourseRequest)) {
            errors.add("Request không được null");
        }

        if (ObjectUtils.isEmpty(wishlistCourseRequest.getCourseId())) {
            errors.add("BlogId không được null");
        }

        if (!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }


        try {
            wishlistService.createWishlist(wishlistCourseRequest);
            // Trả về phản hồi thành công với wishlist vừa tạo
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(""));
        } catch (NotFoundException ex) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
            return;
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi server
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }

    }

    @Override
    @IsAuthenticated
    @HasPermission("WISHLIST_COURSE")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của wishlist không được để trống"));
            return;
        }

        Long wishlistId;

        try {
            wishlistId = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Id của wishlist không hợp lệ"));
            return;
        }

        try {
            wishlistService.deleteWishlist(wishlistId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Xóa wishlist thành công"));
        } catch (ForbiddenException ex) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson(ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi Server"));
        }

    }
}
