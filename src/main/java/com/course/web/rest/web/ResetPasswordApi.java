package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.request.ResetPasswordRequest;
import com.course.service.AccountService;
import com.course.service.impl.AccountServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/reset-password")
public class ResetPasswordApi extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AccountService accountService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        accountService = getBean(AccountServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();
        ResetPasswordRequest resetPasswordRequest = gson.fromJson(req.getReader(), ResetPasswordRequest.class);

        if (resetPasswordRequest.getToken() == null || resetPasswordRequest.getToken().isEmpty()) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Yêu cầu không hợp lệ"));
        }
        if (resetPasswordRequest.getNewPassword() == null || resetPasswordRequest.getNewPassword().isEmpty() ||
                resetPasswordRequest.getConfirmPassword() == null || resetPasswordRequest.getConfirmPassword().isEmpty()) {
            errors.add("Dữ liệu không được để trống");
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            errors.add("Mật khẩu không trùng khớp");
        }
        if (errors.size() > 0) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errors));
        }
        try {
            accountService.resetPassword(resetPasswordRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Đặt lại mật khẩu thành công"));
        } catch (RuntimeException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Lỗi: " + e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }
}

