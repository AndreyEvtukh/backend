package com.portfolio.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ResendEmailService {

    private static final String RESEND_API_URL =
            "https://api.resend.com/emails";

    private final RestClient restClient;
    private final String apiKey;
    private final String from;

    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from}") String from
    ) {
        this.apiKey = apiKey;
        this.from = from;

        this.restClient = RestClient
                .builder()
                .baseUrl(RESEND_API_URL)
                .build();
    }

    public void send(
            String to,
            String subject,
            String text
    ) {
        Map<String, Object> request = Map.of(
                "from", from,
                "to", new String[]{to},
                "subject", subject,
                "text", text
        );

        restClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}