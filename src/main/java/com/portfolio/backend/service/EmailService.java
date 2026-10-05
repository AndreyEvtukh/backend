package com.portfolio.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class EmailService {

    private static final String BREVO_API_URL =
            "https://api.brevo.com/v3/smtp/email";

    private final RestClient restClient;

    private final String apiKey;
    private final String from;
    private final String fromName;

    public EmailService(
            @Value("${brevo.api-key}") String apiKey,
            @Value("${mail.from}") String from,
            @Value("${mail.from-name:Andrey Evtukh}") String fromName
    ) {
        this.apiKey = apiKey;
        this.from = from;
        this.fromName = fromName;

        this.restClient = RestClient.builder()
                .baseUrl(BREVO_API_URL)
                .build();
    }

    /**
     * Sends registration verification code.
     */
    public void sendVerificationCode(
            String email,
            String code
    ) {
        String subject = "Your Portfolio verification code";

        String text = """
                Hello,

                Your verification code is: %s

                This code will expire in 5 minutes.

                If you did not request this code, you can safely ignore this email.

                Best regards,
                Andrey Evtukh
                """.formatted(code);

        log.info("=== Brevo verification email: START ===");
        log.info("From: {} <{}>", fromName, from);
        log.info("To: {}", email);
        log.info("Subject: {}", subject);
        log.info("Verification code: {}", code);

        send(
                email,
                null,
                subject,
                text
        );

        log.info("=== Brevo verification email: SUCCESS ===");
    }

    /**
     * Sends contact form message.
     *
     * The visitor's email is used as Reply-To,
     * while the message itself is sent to the portfolio owner's email.
     */
    public void sendMessage(
            String userName,
            String email,
            String text
    ) {
        String subject = "[Portfolio] Contact from: %s <%s>"
                .formatted(userName, email);

        log.info("=== Brevo contact email: START ===");
        log.info("From: {} <{}>", fromName, from);
        log.info("Reply-To: {}", email);
        log.info("To: {}", from);
        log.info("Subject: {}", subject);
        log.info("Message length: {}", text != null ? text.length() : 0);

        send(
                from,
                new ReplyTo(email, userName),
                subject,
                text
        );

        log.info("=== Brevo contact email: SUCCESS ===");
    }

    private void send(
            String recipient,
            ReplyTo replyTo,
            String subject,
            String text
    ) {
        BrevoEmailRequest request = new BrevoEmailRequest(
                new Sender(fromName, from),
                List.of(new Recipient(recipient)),
                replyTo,
                subject,
                text
        );

        try {
            BrevoEmailResponse response = restClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("api-key", apiKey)
                    .body(request)
                    .retrieve()
                    .body(BrevoEmailResponse.class);

            String messageId = response != null
                    ? response.messageId()
                    : null;

            log.info("Brevo API accepted email");
            log.info("Brevo messageId: {}", messageId);

        } catch (Exception e) {
            log.error("Brevo API email sending failed", e);
            throw new IllegalStateException(
                    "Failed to send email via Brevo API",
                    e
            );
        }
    }

    private record BrevoEmailRequest(
            Sender sender,
            List<Recipient> to,
            ReplyTo replyTo,
            String subject,
            String textContent
    ) {
    }

    private record Sender(
            String name,
            String email
    ) {
    }

    private record Recipient(
            String email
    ) {
    }

    private record ReplyTo(
            String email,
            String name
    ) {
    }

    private record BrevoEmailResponse(
            String messageId
    ) {
    }
}