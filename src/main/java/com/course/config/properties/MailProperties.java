package com.course.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Setter
public class MailProperties {
    private static final MailProperties INSTANCE = new MailProperties();
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean auth;
    private boolean starttlsEnable;

    private MailProperties() {
        loadProperties();
    }

    public static MailProperties getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file 'application.yml' not found in classpath.");
            }
            Map<String, Object> config = new Yaml().load(inputStream);

            Map<String, Object> smtpConfig = (Map<String, Object>) config.get("smtp");
            this.host = (String) smtpConfig.get("host");
            this.port = ((Number) smtpConfig.get("port")).intValue();
            this.username = (String) smtpConfig.get("username");
            this.password = (String) smtpConfig.get("password");

            Map<String, Object> mailSmtpConfig = (Map<String, Object>) ((Map<String, Object>) config.get("mail")).get("smtp");
            this.auth = Boolean.parseBoolean(mailSmtpConfig.get("auth").toString());
            this.starttlsEnable = Boolean.parseBoolean(((Map<String, Object>) mailSmtpConfig.get("starttls")).get("enable").toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mail configuration", e);
        }
    }
}