package com.portfolio.backend.dto.auth.login;

import java.util.UUID;

public record LoginPayloadDTO(boolean success,
                              String message,
                              String token,
                              UUID id,
                              String username,
                              String role,
                              String email) {

    public static LoginPayloadDTO ok(LoginOutputDTO result) {
        return new LoginPayloadDTO(
                true,
                null,
                result.token(),
                result.id(),
                result.username(),
                result.role(),
                result.email());
    }

    public static LoginPayloadDTO fail(String message) {
        return new LoginPayloadDTO(
                false, message,
                null,
                null,
                null,
                null,
                null);
    }
}