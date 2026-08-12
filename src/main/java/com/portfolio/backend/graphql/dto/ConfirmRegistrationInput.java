package com.portfolio.backend.graphql.dto;

public record ConfirmRegistrationInput(
        String email,
        String code
) {
}

