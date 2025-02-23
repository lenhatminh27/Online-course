package com.course.service.async;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String htmlContent);
}
