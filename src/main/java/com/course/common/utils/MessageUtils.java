package com.course.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtils {
    public static final String LOGIN_FAIL = "Invalid username or password!";
    public static final String AUTHENTICATION_REQUIRED = "Body of authentication is required";
    public static final String EMAIL_NOT_NULL = "Email is required";
    public static final String PASSWORD_NOT_NULL = "Password is required";
}
