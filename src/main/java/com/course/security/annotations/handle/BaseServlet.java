package com.course.security.annotations.handle;


import com.course.common.utils.ResponseUtils;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.context.AuthenticationContext;
import com.course.security.context.AuthenticationContextHolder;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Class<?> clazz = this.getClass();
        Gson gson = new Gson();
        try {
            // Xác định phương thức xử lý (doGet, doPost, doPut, doDelete) dựa trên HTTP method
            String methodName = "do" + req.getMethod().toUpperCase().charAt(0) + req.getMethod().toLowerCase().substring(1);
            Method method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // Cho phép truy cập vào phương thức protected hoặc private
            method.setAccessible(true);


            if (clazz.isAnnotationPresent(IsAuthenticated.class) || method.isAnnotationPresent(IsAuthenticated.class)) {
                if (!isAuthenticated()) {
                    ResponseUtils.writeResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, gson.toJson("Unauthorized"));
                    return;
                }
            }
            HasPermission permissionAnnotation = clazz.getAnnotation(HasPermission.class);
            if (permissionAnnotation == null) {
                permissionAnnotation = method.getAnnotation(HasPermission.class);
            }
            if (permissionAnnotation != null) {
                String requiredPermission = permissionAnnotation.value();
                if (!hasPermission(requiredPermission)) {
                    ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN, gson.toJson("Access Denied"));
                    return;
                }
            }
            // Gọi phương thức xử lý chính xác
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, gson.toJson("Method Not Allowed"));
            return;
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Error invoking method"));
        }
    }


    private boolean isAuthenticated() {
        AuthenticationContext context = AuthenticationContextHolder.getContext();
        return context != null;
    }

    private boolean hasPermission(String permission) {
        AuthenticationContext context = AuthenticationContextHolder.getContext();
        return context != null && context.getAuthorities().contains(permission);
    }
}

