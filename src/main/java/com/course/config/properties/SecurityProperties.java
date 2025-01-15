package com.course.config.properties;


public interface SecurityProperties {
    String jwtSecret = "MWM0NzE4MDk2MGQwMWFkNjZiNDQ5ZTJkMTJjYTE2N2M1YTFhY2E3M2UzMzBlMGMzZjU3OGVhMGQwMmQyZGM5OTgyODJlOWIwZGVhMzJkOTUzNTdlMjM4ZDIxMTk0YjgzNmEzNDNlMTBjZTMwMGMyNjgzYTc2ZTlmZjE5MzZkZmM";
    //# Access token is valid 15 minute 900
    long jwtAccessExpiration = 900;
    //# Refresh token is valid 3 days
    long jwtRefreshExpiration = 259200;
    //# Remember me is valid 30 days
    long rememberMeExpiration = 2592000;
}
