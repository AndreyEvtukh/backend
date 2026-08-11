package com.portfolio.backend.service;

import com.portfolio.backend.model.User;
import com.portfolio.backend.model.VerificationCode;
import com.portfolio.backend.model.VerificationCode.Type;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.repository.VerificationCodeRepository;
import com.portfolio.backend.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.verification-code-ttl-minutes:15}")
    private long codeTtlMinutes;

    /**
     * Шаг 1: email + username + password → 6-значный код на почту.
     */
    @Transactional
    public String initiateRegistration(String email, String username, String password) {
        email = email.trim().toLowerCase();

        userRepository.findByEmail(email).ifPresent(u -> {
            if (u.isRegistered()) {
                throw new IllegalArgumentException("Email already registered");
            }
        });

        String code = generateVerificationCode();
        String passwordHash = passwordEncoder.encode(password);
        String payload = username + "\n" + passwordHash;

        VerificationCode token = new VerificationCode();
        token.setEmail(email);
        token.setCode(code);
        token.setType(Type.REGISTER);
        token.setPayload(payload);
        token.setExpiresAt(Instant.now().plus(codeTtlMinutes, ChronoUnit.MINUTES));
        token.setUsed(false);

        verificationCodeRepository.save(token);
        sendVerificationEmail(email, code);

        return "Verification code sent to " + email;
    }

    /**
     * Шаг 2: email + code → создаём User (registered=true), возвращаем JWT.
     */
    @Transactional
    public String verifyAndCreateUser(String email, String verificationCode) {
        email = email.trim().toLowerCase();

        VerificationCode token = verificationCodeRepository
                .findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(email, Type.REGISTER)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or verification code"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Verification code expired");
        }
        if (!token.getCode().equals(verificationCode.trim())) {
            throw new IllegalArgumentException("Invalid email or verification code");
        }

        String payload = token.getPayload();
        if (payload == null || !payload.contains("\n")) {
            throw new IllegalStateException("Invalid registration payload");
        }

        int sep = payload.indexOf('\n');
        String username = payload.substring(0, sep);
        String passwordHash = payload.substring(sep + 1);

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setRegistered(true);
        user.setEnabled(true);
        userRepository.save(user);

        token.setUsed(true);
        verificationCodeRepository.save(token);

        return jwtService.generateToken(user);
    }

    private String generateVerificationCode() {
        int code = random.nextInt(1_000_000);
        return String.format("%06d", code);
    }

    private void sendVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification Code");
        message.setText(
                "Your verification code is: " + code + "\n\n" +
                        "This code expires in " + codeTtlMinutes + " minutes."
        );
        mailSender.send(message);
    }
}