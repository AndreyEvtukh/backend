package com.portfolio.backend.mappers;

import com.portfolio.backend.dto.auth.verify.VerificationCodeDTO;
import com.portfolio.backend.entity.VerificationCode;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface VerificationCodeMapper {
    VerificationCode toEntity(VerificationCodeDTO dto);

    VerificationCodeDTO toDTO(VerificationCode code);
}
