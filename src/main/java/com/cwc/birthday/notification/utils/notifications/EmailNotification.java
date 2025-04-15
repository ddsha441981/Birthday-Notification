package com.cwc.birthday.notification.utils.notifications;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailNotification {

    private static final Logger log = LoggerFactory.getLogger(EmailNotification.class);

    private final JavaMailSender mailSender;

    public EmailNotification(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // TODO: Implementation Email sending logic
    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml);
            mailSender.send(message);

            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
