package com.portfolio.backend.dto.auth.sendEmail;

public record SendEmailPayloadDTO(boolean success, String message) {
    public static SendEmailPayloadDTO ok() {
        return new SendEmailPayloadDTO(true, null);
    }

    public static SendEmailPayloadDTO fail(String message) {
        return new SendEmailPayloadDTO(false, message);
    }
}
