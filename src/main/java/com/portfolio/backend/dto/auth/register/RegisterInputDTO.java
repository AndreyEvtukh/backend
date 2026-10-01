package com.portfolio.backend.dto.auth.register;

public record RegisterInputDTO(
        String email,
        String username,
        String password
) {}