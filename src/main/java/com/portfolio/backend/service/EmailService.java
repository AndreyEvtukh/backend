package com.portfolio.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("Your verification code");

            helper.setText(
                    """
                            <html>
                                <body>
                                    <h2>Email verification</h2>
                                    <p>Your verification code is:</p>
                                    <h1>%s</h1>
                                    <p>This code will expire in 5 minutes.</p>
                                    <p>If you did not request this code, you can ignore this email.</p>
                                </body>
                            </html>
                            """.formatted(code),
                    true
            );

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send verification email", e);
        }
    }

    public void sendMessage(
            String userName,
            String email,
            String text
    ) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");

            log.info("=== Portfolio contact email: START ===");
            log.info("User name: {}", userName);
            log.info("User email: {}", email);
            log.info("From: {}", from);
            log.info("Reply-To: {}", email);
            log.info("To: {}", from);
            log.info("Subject: [Portfolio] Contact from: {} <{}>", userName, email);
            log.info("Message length: {}", text != null ? text.length() : 0);
            log.debug("Message text: {}", text);

            helper.setFrom(from);
            helper.setReplyTo(email);
            helper.setTo(from);
            helper.setSubject("[Portfolio] Contact from: %s <%s>".formatted(userName, email));
            helper.setText(text);

            mailSender.send(mail);

        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send portfolio contact email", e);
        }
    }
}