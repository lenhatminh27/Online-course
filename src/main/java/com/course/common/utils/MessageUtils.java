package com.course.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtils {
    public static final String LOGIN_FAIL = "Tên đăng nhập hoặc mật khẩu không hợp lệ!";
    public static final String AUTHENTICATION_REQUIRED = "Yêu cầu có thông tin xác thực";
    public static final String EMAIL_NOT_NULL = "Email là bắt buộc";
    public static final String PASSWORD_NOT_NULL = "Mật khẩu là bắt buộc";

}
