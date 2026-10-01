package com.portfolio.backend.resolvers;

import com.portfolio.backend.dto.*;
import com.portfolio.backend.dto.auth.login.LoginInputDTO;
import com.portfolio.backend.dto.auth.login.LoginOutputDTO;
import com.portfolio.backend.dto.auth.login.LoginPayloadDTO;

import com.portfolio.backend.dto.auth.logout.LogoutInputDTO;
import com.portfolio.backend.dto.auth.register.RegisterInputDTO;
import com.portfolio.backend.dto.auth.register.RegisterPayloadDTO;

import com.portfolio.backend.dto.auth.reset.ResetPasswordInputDTO;
import com.portfolio.backend.dto.auth.reset.ResetPasswordOutputDTO;
import com.portfolio.backend.dto.auth.reset.ResetPasswordPayloadDTO;
import com.portfolio.backend.dto.auth.sendEmail.SendEmailInputDTO;
import com.portfolio.backend.dto.auth.sendEmail.SendEmailPayloadDTO;
import com.portfolio.backend.dto.auth.verify.VerificationCodeDTO;
import com.portfolio.backend.dto.auth.verify.VerifyInputDTO;
import com.portfolio.backend.dto.auth.verify.VerifyOutputDTO;
import com.portfolio.backend.mappers.UserMapper;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.service.AuthService;

import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthResolver {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Environment environment;

    @QueryMapping
    public UserDTO me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO)
                .orElse(null);

    }

    @MutationMapping
    public LoginPayloadDTO login(
            @Argument LoginInputDTO input,
            GraphQLContext context
    ) {
        try {
            LoginOutputDTO result = authService.login(input.email(), input.password());

            context.put("access_token", result.token());

            return LoginPayloadDTO.ok(result);

        } catch (IllegalArgumentException e) {
            return LoginPayloadDTO.fail(e.getMessage());
        }
    }

    @MutationMapping
    public RegisterPayloadDTO register(@Argument RegisterInputDTO input) {
        try {
            VerificationCodeDTO result = authService.register(input);

            String message = "Verification code sent to email " + result.getEmail();
            String expiresAt = result.getExpiresAt().toString();
            return RegisterPayloadDTO.ok(message, expiresAt);
        } catch (IllegalArgumentException e) {
            return RegisterPayloadDTO.fail(e.getMessage());
        }
    }

    @MutationMapping
    public AuthPayload verify(@Argument VerifyInputDTO input, GraphQLContext context) {
        try {
            VerifyOutputDTO result = authService.verify(
                    input.getEmail(),
                    input.getCode()
            );
            context.put("access_token", result.getToken());
            return AuthPayload.ok(result.getId(), result.getToken(), result.getUsername(), result.getRole(), result.getEmail());
        } catch (IllegalArgumentException e) {
            return AuthPayload.fail(e.getMessage());
        }
    }

    @MutationMapping
    public ResetPasswordPayloadDTO resetPassword(@Argument ResetPasswordInputDTO input) {
        try {
            authService.resetPassword(
                    input.email(),
                    input.password()
            );
            return ResetPasswordPayloadDTO.ok();
        } catch (IllegalArgumentException e) {
            return ResetPasswordPayloadDTO.fail(e.getMessage());
        }
    }

    @MutationMapping
    public SendEmailPayloadDTO sendEmail(@Argument SendEmailInputDTO input) {
        try {
            authService.sendEmail(
                    input.userName(),
                    input.email(),
                    input.message()
            );
            return SendEmailPayloadDTO.ok();
        } catch (IllegalArgumentException e) {
            return SendEmailPayloadDTO.fail(e.getMessage());
        }
    }

    @QueryMapping
    public AuthPayload emailExists(@Argument String email) {
        try {
            LoginOutputDTO result = authService.emailExists(email);
            return AuthPayload.ok(result.id(), result.token(), result.username(), result.role(), result.email());
        } catch (IllegalArgumentException e) {
            return AuthPayload.fail(e.getMessage());
        }
    }

    @MutationMapping
    public LogoutInputDTO logout(GraphQLContext context) {
        context.put("clear_access_token", true);
        return new LogoutInputDTO(true, "Logged out");
    }
}