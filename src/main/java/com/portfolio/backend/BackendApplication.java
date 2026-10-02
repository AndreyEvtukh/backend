package com.portfolio.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        log.info("=> DB_USER configured: " + isConfigured("DB_USER"));
        log.info("=> DB_PASSWORD configured: " + isConfigured("DB_PASSWORD"));
        log.info("=> RESEND_API_KEY configured: " + isConfigured("RESEND_API_KEY"));
        log.info("=> JWT_SECRET configured: " + isConfigured("JWT_SECRET"));

        SpringApplication.run(com.portfolio.backend.BackendApplication.class, args);
    }

    private static boolean isConfigured(String name) {
        String value = System.getenv(name);
        return value != null && !value.isBlank();
    }
}
