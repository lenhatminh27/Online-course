package com.course.security.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationContextHolder {

    private static final ThreadLocal<AuthenticationContext> contextHolder = new ThreadLocal<>();

    public static void setContext(AuthenticationContext context) {
        contextHolder.set(context);
    }

    public static AuthenticationContext getContext() {
        return contextHolder.get();
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}

