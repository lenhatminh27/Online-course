package com.course.service.async.impl;

import com.course.config.properties.MailProperties;
import com.course.core.bean.annotations.Service;
import com.course.core.scheduling.annotations.Async;
import com.course.service.async.EmailService;
import lombok.RequiredArgsConstructor;

import javax.mail.*;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final MailProperties mailProperties = MailProperties.getInstance();

    @Async
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(mailProperties.isAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(mailProperties.isStarttlsEnable()));
        props.put("mail.smtp.host", mailProperties.getHost());
        props.put("mail.smtp.port", mailProperties.getPort());

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProperties.getUsername(), "OCMS"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText(subject, StandardCharsets.UTF_8.name(), "B"));

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Gửi email thành công");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi mail: " + e.getMessage());
        }
    }
}