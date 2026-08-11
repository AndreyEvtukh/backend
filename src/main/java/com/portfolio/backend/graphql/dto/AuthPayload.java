package com.portfolio.backend.graphql.dto;

public record AuthPayload(boolean success, String token, String message) {
    public static AuthPayload ok(String token) {
        return new AuthPayload(true, token, null);
    }

    public static AuthPayload fail(String message) {
        return new AuthPayload(false, null, message);
    }
}