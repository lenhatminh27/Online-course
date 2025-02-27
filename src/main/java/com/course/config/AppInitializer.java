package com.course.config;

import com.course.common.utils.HibernateUtils;
import com.course.config.filter.AccessTokenFilter;
import com.course.config.filter.RefreshTokenFilter;
import com.course.config.properties.MinioProperties;
import com.course.config.properties.SecurityProperties;
import com.course.config.properties.SepayProperties;
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

        SecurityProperties securityProperties = SecurityProperties.getInstance();

        MinioProperties minioProperties = MinioProperties.getInstance();

        SepayProperties sepayProperties = SepayProperties.getInstance();

        // Log properties for debugging (remove in production)
        System.out.println("Loaded Security Properties:");
        System.out.println("JWT Secret: " + securityProperties.getJwtSecret());
        System.out.println("Access Token Expiration: " + securityProperties.getJwtAccessExpiration());
        System.out.println("Refresh Token Expiration: " + securityProperties.getJwtRefreshExpiration());
        System.out.println("Remember Me Expiration: " + securityProperties.getRememberMeExpiration());

        System.out.println("Loaded Minio Properties:");
        System.out.println("Access key: " + minioProperties.getAccessKey());
        System.out.println("Secret key: " + minioProperties.getSecretKey());
        System.out.println("Url : " + minioProperties.getUrl());

        // Đăng ký AccessTokenFilter cho các URL bắt đầu bằng /api
        FilterRegistration.Dynamic accessTokenFilter = servletContext.addFilter("accessTokenFilter", new AccessTokenFilter());
        accessTokenFilter.addMappingForUrlPatterns(null, true, "/api/*");

        // Đăng ký RefreshTokenFilter cho các URL /refresh-token
        FilterRegistration.Dynamic refreshTokenFilter = servletContext.addFilter("refreshTokenFilter", new RefreshTokenFilter());
        refreshTokenFilter.addMappingForUrlPatterns(null, true, "/refresh-token");

        // Khởi tạo sẵn Hibernate SessionFactory
        HibernateUtils.getSessionFactory();

        // Không đăng ký bất kỳ filter nào cho các URL /auth và /register
        // Các API này sẽ không bị bất kỳ filter nào áp dụng
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtils.shutdown();
    }

}
