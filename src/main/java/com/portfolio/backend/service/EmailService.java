package com.portfolio.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@andrey-evtukh.vercel.app");
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
            throw new IllegalStateException("Failed to create verification email", e);
        }
    }

    public void sendMessage(String userName, String email, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@andrey-evtukh.vercel.app");
            helper.setTo(from);
            helper.setSubject("[no-reply] Portfolio contact from: %s, <%s>".formatted(userName, email));

            helper.setText(String.valueOf(text), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to create verification email", e);
        }
    }
}