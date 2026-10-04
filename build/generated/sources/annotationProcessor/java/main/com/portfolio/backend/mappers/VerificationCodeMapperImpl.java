package com.portfolio.backend.mappers;

import com.portfolio.backend.dto.auth.verify.VerificationCodeDTO;
import com.portfolio.backend.entity.VerificationCode;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T12:58:27+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.7.1.jar, environment: Java 21.0.12.1 (Microsoft)"
)
@Component
public class VerificationCodeMapperImpl implements VerificationCodeMapper {

    @Override
    public VerificationCode toEntity(VerificationCodeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setEmail( dto.getEmail() );
        verificationCode.setCode( dto.getCode() );
        verificationCode.setUsername( dto.getUsername() );
        verificationCode.setExpiresAt( dto.getExpiresAt() );

        return verificationCode;
    }

    @Override
    public VerificationCodeDTO toDTO(VerificationCode code) {
        if ( code == null ) {
            return null;
        }

        String code1 = null;
        String username = null;
        String email = null;
        Instant expiresAt = null;

        code1 = code.getCode();
        username = code.getUsername();
        email = code.getEmail();
        expiresAt = code.getExpiresAt();

        String role = null;

        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO( code1, username, role, email, expiresAt );

        return verificationCodeDTO;
    }
}
