package com.portfolio.backend.resolvers;

import com.portfolio.backend.graphql.dto.*;
import com.portfolio.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthResolver {

    private final AuthService authService;

    // -------------------------------------------------------------------------
    // Query
    // -------------------------------------------------------------------------

    @QueryMapping
    public EmailExistsPayload emailExists(
            @Argument String email
    ) {
        boolean exists = authService.emailExists(email);

        return new EmailExistsPayload(exists);
    }

    // -------------------------------------------------------------------------
    // Registration — step 1
    // -------------------------------------------------------------------------

    @MutationMapping
    public MessagePayload requestRegistration(
            @Argument RegisterRequestInput input
    ) {
        try {
            authService.requestRegistration(
                    input.email(),
                    input.username(),
                    input.password_hash()
            );

            return MessagePayload.ok(
                    "Verification code sent to email"
            );

        } catch (IllegalArgumentException e) {
            return MessagePayload.fail(
                    e.getMessage()
            );

        } catch (Exception e) {
            return MessagePayload.fail(
                    "Unable to send verification email"
            );
        }
    }

    // -------------------------------------------------------------------------
    // Registration — step 2
    // -------------------------------------------------------------------------

    @MutationMapping
    public AuthPayload confirmRegistration(
            @Argument ConfirmRegistrationInput input
    ) {
        try {
            String token = authService.confirmRegistration(
                    input.email(),
                    input.code()
            );

            return AuthPayload.ok(token);

        } catch (IllegalArgumentException e) {
            return AuthPayload.fail(
                    e.getMessage()
            );
        }
    }

    // -------------------------------------------------------------------------
    // Login
    // -------------------------------------------------------------------------

    @MutationMapping
    public AuthPayload login(
            @Argument LoginInput input
    ) {
        try {
            String token = authService.login(
                    input.email(),
                    input.password()
            );

            return AuthPayload.ok(token);

        } catch (IllegalArgumentException e) {
            return AuthPayload.fail(
                    e.getMessage()
            );
        }
    }

    // -------------------------------------------------------------------------
    // Password reset — request
    // -------------------------------------------------------------------------

    @MutationMapping
    public MessagePayload requestPasswordReset(
            @Argument String email
    ) {
        try {
            authService.requestPasswordReset(email);

        } catch (Exception e) {
            // Не раскрываем существование email
            // и не возвращаем внутреннюю ошибку клиенту.
        }

        return MessagePayload.ok(
                "If the email exists, a reset message has been sent"
        );
    }

    // -------------------------------------------------------------------------
    // Password reset — new password
    // -------------------------------------------------------------------------

    @MutationMapping
    public MessagePayload resetPassword(
            @Argument ResetPasswordInput input
    ) {
        try {
            authService.resetPassword(
                    input.email(),
                    input.code(),
                    input.newPassword()
            );

            return MessagePayload.ok(
                    "Password updated"
            );

        } catch (IllegalArgumentException e) {
            return MessagePayload.fail(
                    e.getMessage()
            );
        }
    }
}