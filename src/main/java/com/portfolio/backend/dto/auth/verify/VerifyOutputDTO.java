package com.portfolio.backend.dto.auth.verify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class VerifyOutputDTO {
    private UUID id;
    private String token;
    private String username;
    private String role;
    private String email;
}

