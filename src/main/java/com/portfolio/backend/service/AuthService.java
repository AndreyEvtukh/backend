package com.portfolio.backend.service;

import com.portfolio.backend.jwt.JwtService;
import com.portfolio.backend.model.User;
import com.portfolio.backend.model.VerificationCode;
import com.portfolio.backend.model.VerificationCode.Type;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResendEmailService emailService;
    private final JwtService jwtService;

    private final SecureRandom random = new SecureRandom();

    @Value("${app.verification-code-ttl-minutes:15}")
    private long codeTtlMinutes;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    // -------------------------------------------------------------------------
    // 1. РЕГИСТРАЦИЯ — шаг 1: запрос кода
    // -------------------------------------------------------------------------

    @Transactional
    public void requestRegistration(
            String email,
            String username,
            String password
    ) {
        email = email.trim().toLowerCase();

        // Проверяем, не зарегистрирован ли уже пользователь
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.isRegistered()) {
                throw new IllegalArgumentException("Email already registered");
            }
        });

        String code = generateSixDigitCode();

        String passwordHash = passwordEncoder.encode(password);

        // Храним username + hash пароля до подтверждения email.
        String payload = username + "\n" + passwordHash;

        VerificationCode vc = new VerificationCode();

        vc.setEmail(email);
        vc.setCode(code);
        vc.setType(Type.REGISTER);
        vc.setPayload(payload);
        vc.setExpiresAt(
                Instant.now().plus(codeTtlMinutes, ChronoUnit.MINUTES)
        );
        vc.setUsed(false);

        codeRepository.save(vc);

        emailService.send(
                email,
                "Код подтверждения регистрации",
                """
                Ваш код подтверждения: %s
                
                Код действует %d минут.
                
                Если вы не регистрировались, просто проигнорируйте это письмо.
                """.formatted(code, codeTtlMinutes)
        );
    }

    // -------------------------------------------------------------------------
    // 1. РЕГИСТРАЦИЯ — шаг 2: подтверждение кода
    // -------------------------------------------------------------------------

    @Transactional
    public String confirmRegistration(
            String email,
            String code
    ) {
        email = email.trim().toLowerCase();

        VerificationCode vc = codeRepository
                .findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(
                        email,
                        Type.REGISTER
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Code not found")
                );

        if (vc.isUsed()) {
            throw new IllegalArgumentException("Code already used");
        }

        if (vc.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Code expired");
        }

        if (!vc.getCode().equals(code.trim())) {
            throw new IllegalArgumentException("Invalid code");
        }

        String payload = vc.getPayload();

        if (payload == null || !payload.contains("\n")) {
            throw new IllegalStateException("Invalid payload");
        }

        int separator = payload.indexOf('\n');

        String username = payload.substring(0, separator);
        String passwordHash = payload.substring(separator + 1);

        User user = userRepository
                .findByEmail(email)
                .orElseGet(User::new);

        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setRegistered(true);
        user.setEnabled(true);

        userRepository.save(user);

        vc.setUsed(true);
        codeRepository.save(vc);

        return jwtService.generateToken(user);
    }

    // -------------------------------------------------------------------------
    // 2. ПРОВЕРКА EMAIL
    // -------------------------------------------------------------------------

    public boolean emailExists(String email) {
        return userRepository
                .findByEmail(email.trim().toLowerCase())
                .map(User::isRegistered)
                .orElse(false);
    }

    // -------------------------------------------------------------------------
    // 3. ВОССТАНОВЛЕНИЕ ПАРОЛЯ — запрос
    // -------------------------------------------------------------------------

    @Transactional
    public void requestPasswordReset(String email) {
        email = email.trim().toLowerCase();

        var userOpt = userRepository
                .findByEmail(email)
                .filter(User::isRegistered);

        // Не раскрываем существование email
        if (userOpt.isEmpty()) {
            return;
        }

        String code = generateSixDigitCode();

        VerificationCode vc = new VerificationCode();

        vc.setEmail(email);
        vc.setCode(code);
        vc.setType(Type.RESET_PASSWORD);
        vc.setExpiresAt(
                Instant.now().plus(codeTtlMinutes, ChronoUnit.MINUTES)
        );
        vc.setUsed(false);

        codeRepository.save(vc);

        String link = frontendUrl
                + "/reset-password?email="
                + email
                + "&code="
                + code;

        emailService.send(
                email,
                "Восстановление пароля",
                """
                Код для смены пароля: %s
                
                Или перейдите по ссылке:
                %s
                
                Код действует %d минут.
                
                Если вы не запрашивали восстановление пароля,
                просто проигнорируйте это письмо.
                """.formatted(
                        code,
                        link,
                        codeTtlMinutes
                )
        );
    }

    // -------------------------------------------------------------------------
    // 3. ВОССТАНОВЛЕНИЕ ПАРОЛЯ — новый пароль
    // -------------------------------------------------------------------------

    @Transactional
    public void resetPassword(
            String email,
            String code,
            String newPassword
    ) {
        email = email.trim().toLowerCase();

        VerificationCode vc = codeRepository
                .findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(
                        email,
                        Type.RESET_PASSWORD
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Code not found")
                );

        if (vc.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Code expired");
        }

        if (!vc.getCode().equals(code.trim())) {
            throw new IllegalArgumentException("Invalid code");
        }

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        vc.setUsed(true);
        codeRepository.save(vc);
    }

    // -------------------------------------------------------------------------
    // 4. ЛОГИН → JWT
    // -------------------------------------------------------------------------

    public String login(
            String email,
            String password
    ) {
        User user = userRepository
                .findByEmail(email.trim().toLowerCase())
                .filter(User::isRegistered)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid credentials")
                );

        if (!passwordEncoder.matches(
                password,
                user.getPasswordHash()
        )) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

    // -------------------------------------------------------------------------
    // Генерация 6-значного кода
    // -------------------------------------------------------------------------

    private String generateSixDigitCode() {
        int n = random.nextInt(1_000_000);
        return String.format("%06d", n);
    }
}