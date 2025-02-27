package com.course.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Setter
public class SepayProperties {
    private static final SepayProperties INSTANCE = new SepayProperties();

    private String url;
    private String apiToken;
    private String account;
    private String bank;

    private SepayProperties() {
        loadProperties();
    }

    public static SepayProperties getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file 'application.yml' not found in classpath.");
            }
            Map<String, Object> config = new Yaml().load(inputStream);
            Map<String, Object> sepayConfig = (Map<String, Object>) config.get("sepay");
            this.url = (String) sepayConfig.get("url");
            this.apiToken = (String) sepayConfig.get("api-token");
            this.account = (String) sepayConfig.get("account");
            this.bank = (String) sepayConfig.get("bank");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Sepay configuration", e);
        }
    }
}

