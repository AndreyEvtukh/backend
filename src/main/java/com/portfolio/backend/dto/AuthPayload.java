package com.portfolio.backend.dto;

import java.util.UUID;

public record AuthPayload(
        UUID id,
        String token,
        String message,
        Integer code,
        String username,
        String role,
        String email
) {

    public static AuthPayload ok(UUID id, String token, String username, String role, String email) {
        return new AuthPayload(id, token, null, 200, username, role, email);
    }

    public static AuthPayload fail(String message) {
        return new AuthPayload(null, null, message, null, null, null, null);
    }
}