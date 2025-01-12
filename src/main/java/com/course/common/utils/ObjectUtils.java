package com.course.common.utils;

import jakarta.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectUtils {

    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            Optional<?> optional = (Optional)obj;
            return optional.isEmpty();
        } else if (obj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence)obj;
            return charSequence.isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            Collection<?> collection = (Collection)obj;
            return collection.isEmpty();
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map)obj;
            return map.isEmpty();
        } else {
            return false;
        }
    }
}
