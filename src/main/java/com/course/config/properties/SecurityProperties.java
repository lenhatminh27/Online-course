package com.course.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Setter
public class SecurityProperties {
    private static final SecurityProperties INSTANCE = new SecurityProperties();
    private String jwtSecret;
    private long jwtAccessExpiration;
    private long jwtRefreshExpiration;
    private long rememberMeExpiration;

    private SecurityProperties() {
        loadProperties();
    }

    public static SecurityProperties getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file 'config.yml' not found in classpath.");
            }
            Map<String, Object> config = new Yaml().load(inputStream);

            Map<String, Object> jwtConfig = (Map<String, Object>) ((Map<String, Object>) config.get("security")).get("jwt");
            this.jwtSecret = (String) jwtConfig.get("secret");
            this.jwtAccessExpiration = ((Number) jwtConfig.get("accessExpiration")).longValue();
            this.jwtRefreshExpiration = ((Number) jwtConfig.get("refreshExpiration")).longValue();
            this.rememberMeExpiration = ((Number) jwtConfig.get("rememberMeExpiration")).longValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

}
