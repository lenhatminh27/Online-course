package com.course.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseUtils {

    public static void writeResponse(HttpServletResponse resp, int status, String jsonResponse) throws IOException {
        resp.setStatus(status);
        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonResponse);
        }
    }
}
