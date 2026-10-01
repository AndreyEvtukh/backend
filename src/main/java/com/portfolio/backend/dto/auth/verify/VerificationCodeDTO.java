package com.portfolio.backend.dto.auth.verify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class VerificationCodeDTO {
    private String code;
    private String username;
    private String role;
    private String email;
    private Instant expiresAt;
}

