package com.portfolio.backend.graphql.dto;

public record RegisterRequestInput(
        String email,
        String username,
        String password_hash
) {}