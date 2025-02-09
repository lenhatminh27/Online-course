package com.course.service;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String htmlContent);
}
