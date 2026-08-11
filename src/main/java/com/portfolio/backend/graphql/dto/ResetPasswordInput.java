package com.portfolio.backend.graphql.dto;

public record ResetPasswordInput(String email, String code, String newPassword) {}

