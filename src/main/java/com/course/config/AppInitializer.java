package com.course.config;

import com.course.config.filter.AccessTokenFilter;
import com.course.config.filter.RefreshTokenFilter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        // Đăng ký AccessTokenFilter cho các URL bắt đầu bằng /api
        FilterRegistration.Dynamic accessTokenFilter = servletContext.addFilter("accessTokenFilter", new AccessTokenFilter());
        accessTokenFilter.addMappingForUrlPatterns(null, true, "/api/*");

        // Đăng ký RefreshTokenFilter cho các URL /refresh-token
        FilterRegistration.Dynamic refreshTokenFilter = servletContext.addFilter("refreshTokenFilter", new RefreshTokenFilter());
        refreshTokenFilter.addMappingForUrlPatterns(null, true, "/refresh-token");

        // Không đăng ký bất kỳ filter nào cho các URL /auth và /register
        // Các API này sẽ không bị bất kỳ filter nào áp dụng
    }

}
