package com.portfolio.backend.graphql.dto;

public record MessagePayload(boolean success, String message) {
    public static MessagePayload ok(String message) {
        return new MessagePayload(true, message);
    }
    public static MessagePayload fail(String message) {
        return new MessagePayload(false, message);
    }
}
