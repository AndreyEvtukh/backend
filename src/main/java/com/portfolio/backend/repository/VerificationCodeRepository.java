package com.portfolio.backend.repository;

import com.portfolio.backend.model.VerificationCode;
import com.portfolio.backend.model.VerificationCode.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    Optional<VerificationCode> findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(
            String email, Type type);
}
