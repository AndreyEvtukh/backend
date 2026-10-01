package com.portfolio.backend.dto.auth.login;

import java.util.UUID;

public record LoginOutputDTO(
        String token,
        UUID id,
        String username,
        String role,
        String email
) {}