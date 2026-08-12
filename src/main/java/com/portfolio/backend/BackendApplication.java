package com.portfolio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        System.out.println("DB_USER=" + System.getenv("DB_USER"));
        System.out.println("DB_PASSWORD=" + System.getenv("DB_PASSWORD"));
        System.out.println("RESEND_API_KEY=" + System.getenv("RESEND_API_KEY"));
        SpringApplication.run(com.portfolio.backend.BackendApplication.class, args);
    }
}
