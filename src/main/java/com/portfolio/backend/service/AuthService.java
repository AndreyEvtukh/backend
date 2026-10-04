package com.portfolio.backend.service;

import com.portfolio.backend.dto.auth.register.RegisterInputDTO;
import com.portfolio.backend.dto.auth.reset.ResetPasswordOutputDTO;
//import com.portfolio.backend.dto.auth.sendEmail.SendEmailInputDTO;
import com.portfolio.backend.dto.auth.sendEmail.SendEmailOutputDTO;
import com.portfolio.backend.dto.auth.verify.VerificationCodeDTO;
import com.portfolio.backend.dto.auth.verify.VerifyOutputDTO;
import com.portfolio.backend.exceptions.*;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.entity.VerificationCode;
import com.portfolio.backend.entity.VerificationCode.Type;
import com.portfolio.backend.mappers.VerificationCodeMapper;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.portfolio.backend.dto.auth.login.LoginOutputDTO;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final VerificationCodeMapper verificationCodeMapper;

    private final SecureRandom random = new SecureRandom();

    @Value("${app.verification-code-ttl-minutes:5}")
    private long codeTtlMinutes;

    /**
     *
     * @param email
     * @param password
     * @return
     */
    public LoginOutputDTO login(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(() -> new InputEmailErrorException(normalizedEmail));

        if (!user.isRegistered()) {
            throw new InputEmailErrorException(normalizedEmail);
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InputPasswordErrorException();
        }

        String token = jwtService.generateToken(user);

        return new LoginOutputDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getEmail()
        );
    }

    /**
     *
     * @param input
     * @return
     */
    @Transactional
    public VerificationCodeDTO register(RegisterInputDTO input) {
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        String username = input.username().trim();
        String password = input.password();

        log.info("=> Registration code requested for email: {}", email);

        User existingUser = userRepository
                .findByEmail(email)
                .orElse(null);

        if (existingUser != null && existingUser.isRegistered()) {
            throw new InputEmailAlreadyRegisteredException(email);
        }

        verificationCodeRepository.invalidateActiveCodes(email, Type.REGISTER);

        String code = generateSixDigitCode();
        String passwordHash = passwordEncoder.encode(password);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setType(Type.REGISTER);
        verificationCode.setUsername(username);
        verificationCode.setPasswordHash(passwordHash);
        verificationCode.setExpiresAt(
                Instant.now().plus(codeTtlMinutes, ChronoUnit.MINUTES)
        );
        verificationCode.setUsed(false);

        VerificationCode saved = verificationCodeRepository.save(verificationCode);

        try {
            emailService.sendVerificationCode(email, code);
        } catch (Exception e) {
            log.error(
                    "Failed to send registration email to {}",
                    email,
                    e
            );

            throw new InputSendMailErrorException();
        }

        return verificationCodeMapper.toDTO(saved);
    }

    public VerifyOutputDTO verify(String email, String code) {
        email = email.trim().toLowerCase();

        VerificationCode verificationCode = verificationCodeRepository
                .findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(email, Type.REGISTER)
                .orElseThrow(InputVerifyCodeErrorException::new);

        if (verificationCode.isUsed()) throw new InputVerifyCodeUsedException();
        if (verificationCode.getExpiresAt().isBefore(Instant.now())) throw new InputVerifyCodeExpiredException();
        if (!verificationCode.getCode().equals(code.trim())) throw new InputVerifyCodeErrorException();

        String username = verificationCode.getUsername();
        String passwordHash = verificationCode.getPasswordHash();

        User user = userRepository
                .findByEmail(email)
                .orElseGet(User::new);

        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setRole("USER");
        user.setRegistered(true);
        user.setEnabled(true);

        userRepository.save(user);

        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        String token = jwtService.generateToken(user);

        return new VerifyOutputDTO(user.getId(), token, user.getUsername(), user.getRole(), user.getEmail());
    }

    public LoginOutputDTO emailExists(String email) {
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        String token = jwtService.generateToken(user);
        return new LoginOutputDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getEmail()
        );
    }

    public ResetPasswordOutputDTO resetPassword(
            String email,
            String password
    ) {
        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(() -> new InputEmailErrorException(normalizedEmail));

        if (!user.isRegistered()) {
            throw new InputEmailErrorException(normalizedEmail);
        }

        String passwordHash = passwordEncoder.encode(password);
        user.setPasswordHash(passwordHash);

        User saved = userRepository.save(user);
        return new ResetPasswordOutputDTO(true);
    }

    public SendEmailOutputDTO sendEmail(
            String userName,
            String email,
            String message
    ) {
        String normalizedEmail = email.trim().toLowerCase();

        try {
            emailService.sendMessage(userName, normalizedEmail, message);
            return new SendEmailOutputDTO(true);

        } catch (Exception e) {
            log.error("Failed to send contact email from {}", normalizedEmail, e);
            throw new InputSendMailErrorException();
        }
    }

    private String generateSixDigitCode() {
        int n = random.nextInt(1_000_000);
        return String.format("%06d", n);
    }
}