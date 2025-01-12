package com.course.common.utils;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {

    public static boolean hasText(@Nullable String str) {
        return str != null && !str.isBlank();
    }
}
