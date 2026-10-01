package com.portfolio.backend.dto.auth.reset;

public record ResetPasswordPayloadDTO(boolean success, String message) {
    public static ResetPasswordPayloadDTO ok() {
        return new ResetPasswordPayloadDTO(true, null);
    }

    public static ResetPasswordPayloadDTO fail(String message) {
        return new ResetPasswordPayloadDTO(false, message);
    }
}
