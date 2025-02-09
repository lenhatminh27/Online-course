package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.PasswordResetTokenDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.PasswordResetTokenDAOImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.dto.request.ForgotPasswordRequest;
import com.course.service.AccountService;
import com.course.service.EmailService;
import com.course.service.impl.AccountServiceImpl;
import com.course.service.impl.EmailServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/api/send-reset-password-email")
public class SendResetPasswordEmailApi extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final AccountService accountService;

    public SendResetPasswordEmailApi() {
        AccountDAO accountDAO = new AccountDaoImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        PasswordResetTokenDAO passwordResetTokenDAO = new PasswordResetTokenDAOImpl();
        EmailService emailService = new EmailServiceImpl();
        this.accountService = new AccountServiceImpl(accountDAO, roleDAO, passwordResetTokenDAO, emailService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ForgotPasswordRequest forgotPasswordRequest = gson.fromJson(req.getReader(), ForgotPasswordRequest.class);

        if (forgotPasswordRequest.getEmail() == null || forgotPasswordRequest.getEmail().isEmpty()) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Email không hợp lệ"));
            return;
        }

        try {
            accountService.sendResetPasswordEmail(forgotPasswordRequest);
        } catch (RuntimeException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Không thể gửi email: " + e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }
}
