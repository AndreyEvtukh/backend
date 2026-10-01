package com.portfolio.backend.dto.auth.register;

public record RegisterPayloadDTO(boolean success, String message, String expiresAt) {
    public static RegisterPayloadDTO ok(String message, String expiresAt) {
        return new RegisterPayloadDTO(true, message, expiresAt);
    }

    public static RegisterPayloadDTO fail(String message) {
        return new RegisterPayloadDTO(false, message, null);
    }
}
