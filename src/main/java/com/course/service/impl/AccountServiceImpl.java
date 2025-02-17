package com.course.service.impl;

import com.course.common.utils.PasswordUtils;
import com.course.common.utils.ResponseUtils;
import com.course.common.utils.StringUtils;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.PasswordResetTokenDAO;
import com.course.dao.RoleDAO;
import com.course.dto.request.ChangePasswordRequest;
import com.course.dto.request.ForgotPasswordRequest;
import com.course.dto.request.RegisterRequest;
import com.course.dto.request.ResetPasswordRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.ErrorResponse;
import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;
import com.course.entity.PasswordResetTokenEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.AccountService;
import com.course.service.EmailService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.course.common.utils.PasswordUtils.hashPassword;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    private final RoleDAO roleDAO;

    private final PasswordResetTokenDAO passwordResetTokenDAO;

    private final EmailService emailService;



    @Override
    public AccountResponse getCurrentAccount() {
        String emailCurrent = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = this.accountDAO.findByEmail(emailCurrent);
        List<String> roles = accountCurrent.getRoles().stream()
                .map(RoleEntity::getName)
                .map(ERole::name)
                .toList();
        return new AccountResponse(emailCurrent, null, roles);
    }


    @Override
    public void registerAccount(RegisterRequest registerRequest, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        if (accountDAO.existsByEmail(registerRequest.getEmail())) {
            List<String> error = new ArrayList<>();
            error.add("Email đã tồn tại!");
            ErrorResponse errorResponse = new ErrorResponse(error);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        RoleEntity role = roleDAO.findByName(ERole.LEARNER);
        AccountEntity newAccount = new AccountEntity();
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setPasswordHash(hashPassword(registerRequest.getPassword()));
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setUpdatedAt(LocalDateTime.now());
        newAccount.setRoles(List.of(role));
        accountDAO.save(newAccount);

        AccountProfileEntity accountProfileEntity = new AccountProfileEntity();
        accountProfileEntity.setAccount(newAccount);  // Liên kết AccountProfileEntity với AccountEntity
        accountProfileEntity.setFirstName(registerRequest.getFirstName());
        accountProfileEntity.setLastName(registerRequest.getLastName());
        accountProfileEntity.setCreatedAt(LocalDateTime.now());

        // Lưu AccountProfileEntity vào cơ sở dữ liệu
        accountDAO.saveAccountProfile(accountProfileEntity);
    }

    @Override
    public void updatePassword(ChangePasswordRequest changePasswordRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);

        if (!PasswordUtils.verifyPassword(changePasswordRequest.getCurrentPassword(), accountEntity.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        String hashedNewPassword = PasswordUtils.hashPassword(changePasswordRequest.getNewPassword());
        accountEntity.setPasswordHash(hashedNewPassword);
        accountEntity.setUpdatedAt(LocalDateTime.now());

        accountDAO.update(accountEntity);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.getToken();
        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();

        PasswordResetTokenEntity resetToken = passwordResetTokenDAO.findByToken(token);
        if (resetToken == null) {
            throw new RuntimeException("Yêu cầu không hợp lệ");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Yêu cầu đã hết hạn");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu không trùng khớp");
        }

        AccountEntity account = resetToken.getAccount();
        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        account.setPasswordHash(hashedPassword);
        account.setUpdatedAt(LocalDateTime.now());
        accountDAO.update(account);

        passwordResetTokenDAO.delete(resetToken);
    }


    @Override
    public void sendResetPasswordEmail(ForgotPasswordRequest forgotPasswordRequest) {

        if (!accountDAO.existsByEmail(forgotPasswordRequest.getEmail())) {
            throw new RuntimeException("Email không tồn tại trong hệ thống");
        }

        AccountEntity account = accountDAO.findByEmail(forgotPasswordRequest.getEmail());
        String token;
        do {
            token = StringUtils.generate(6);
        } while (passwordResetTokenDAO.existsByToken(token));

        PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity();
        passwordResetToken.setAccount(account);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        passwordResetTokenDAO.save(passwordResetToken);

        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        String subject = "Thay đổi mật khẩu";
        String message = "<html><body>"
                + "<h3>Để thay đổi mật khẩu của bạn, vui lòng nhấn vào đường link sau:</h3>"
                + "<a href=\"" + resetLink + "\">Thay đổi mật khẩu</a>"
                + "<br><br>"
                + "<p style=\"color: red; font-weight: bold;\">Lưu ý: Đường link này sẽ hết hạn sau 5 phút. Vui lòng thực hiện thay đổi mật khẩu trong thời gian này.</p>"
                + "</body></html>";
        emailService.sendEmail(forgotPasswordRequest.getEmail(), subject, message);
    }
}