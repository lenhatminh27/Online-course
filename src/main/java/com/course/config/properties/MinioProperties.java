package com.course.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Setter
public class MinioProperties {
    private static final MinioProperties INSTANCE = new MinioProperties();
    private String accessKey;
    private String secretKey;
    private String url;

    private MinioProperties() {
        loadProperties();
    }

    public static MinioProperties getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file 'application.yml' not found in classpath.");
            }
            Map<String, Object> config = new Yaml().load(inputStream);

            Map<String, Object> minioConfig = (Map<String, Object>) ((Map<String, Object>) config.get("integration")).get("minio");
            this.accessKey = (String) minioConfig.get("access-key");
            this.secretKey = (String) minioConfig.get("secret-key");
            this.url = (String) minioConfig.get("url");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load MinIO configuration", e);
        }
    }
}

