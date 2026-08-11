package com.portfolio.backend.controller;

import com.portfolio.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // -------------------------------------------------------------------------
    // Логин: email + password → JWT
    // -------------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            String token = authService.login(req.email(), req.password());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // Регистрация — шаг 1: email + username + password → код на почту
    // -------------------------------------------------------------------------
    @PostMapping("/register/request")
    public ResponseEntity<?> registerRequest(@RequestBody RegisterRequest req) {
        try {
            authService.requestRegistration(req.email(), req.username(), req.password());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Verification code sent to email"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Failed to send verification email"));
        }
    }

    // -------------------------------------------------------------------------
    // Регистрация — шаг 2: email + code → user (registered=true) + JWT
    // -------------------------------------------------------------------------
    @PostMapping("/register/confirm")
    public ResponseEntity<?> registerConfirm(@RequestBody ConfirmRequest req) {
        try {
            String token = authService.confirmRegistration(req.email(), req.code());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // Проверка: есть ли email в БД (registered=true)
    // -------------------------------------------------------------------------
    @PostMapping("/email-exists")
    public ResponseEntity<?> emailExists(@RequestBody EmailRequest req) {
        boolean exists = authService.emailExists(req.email());
        return ResponseEntity.ok(Map.of("success", exists));
    }

    // -------------------------------------------------------------------------
    // Сброс пароля — запрос письма
    // -------------------------------------------------------------------------
    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest req) {
        authService.requestPasswordReset(req.email());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "If the email exists, a reset message has been sent"
        ));
    }

    // -------------------------------------------------------------------------
    // Сброс пароля — новый пароль
    // -------------------------------------------------------------------------
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest req) {
        try {
            authService.resetPassword(req.email(), req.code(), req.newPassword());
            return ResponseEntity.ok(Map.of("success", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ===== Request DTOs (records) =====

    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String email, String username, String password) {}
    public record ConfirmRequest(String email, String code) {}
    public record EmailRequest(String email) {}
    public record ResetRequest(String email, String code, String newPassword) {}
}