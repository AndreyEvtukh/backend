package com.portfolio.backend.repository;

import com.portfolio.backend.entity.VerificationCode;
import com.portfolio.backend.entity.VerificationCode.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationCodeRepository
        extends JpaRepository<VerificationCode, Integer> {

    Optional<VerificationCode>
    findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(
            String email,
            Type type
    );

    @Modifying
    @Query("""
                update VerificationCode v
                set v.used = true
                where v.email = :email
                  and v.type = :type
                  and v.used = false
            """)
    int invalidateActiveCodes(@Param("email") String email, @Param("type") Type type);
}