package com.portfolio.backend.dto.auth.verify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyInputDTO {
    private String code;
    private String email;
}

