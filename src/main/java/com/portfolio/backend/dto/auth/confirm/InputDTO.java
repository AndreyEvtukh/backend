package com.portfolio.backend.dto.auth.confirm;

public record InputDTO(
        String email,
        String code
) {
}

