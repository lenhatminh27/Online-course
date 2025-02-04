package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dto.request.ChangePasswordRequest;
import com.course.dto.response.ErrorResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AccountService;
import com.course.service.impl.AccountServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@IsAuthenticated
@WebServlet("/api/change-password")
public class ChangePasswordApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final AccountService accountService;

    public ChangePasswordApi() {
        AccountDAO accountDAO = new AccountDaoImpl();
        this.accountService = new AccountServiceImpl(accountDAO, null);
    }

    @Override
    @IsAuthenticated
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();

        try {
            ChangePasswordRequest changePasswordRequest = gson.fromJson(req.getReader(), ChangePasswordRequest.class);

            if (ObjectUtils.isEmpty(changePasswordRequest)) {
                errors.add("Phản hồi không được rỗng");
            } else {
                if (ObjectUtils.isEmpty(changePasswordRequest.getCurrentPassword())) {
                    errors.add("Mật khẩu hiện tại không được để trống");
                }
                if (ObjectUtils.isEmpty(changePasswordRequest.getNewPassword())) {
                    errors.add("Mật khẩu mới không được để trống");
                }
                if (ObjectUtils.isEmpty(changePasswordRequest.getConfirmPassword())) {
                    errors.add("Mật khẩu xác nhận không được để trống");
                }
                if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getCurrentPassword())) {
                    errors.add("Mật khẩu mới phải khác mật khẩu hiện tại");
                }
                if (!changePasswordRequest.getConfirmPassword().equals(changePasswordRequest.getNewPassword())) {
                    errors.add("Mật khẩu mới không trùng khớp");
                }
            }

            if (!errors.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }

            accountService.updatePassword(changePasswordRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "");
        } catch (RuntimeException e) {
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.getError().add(e.getMessage());
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}