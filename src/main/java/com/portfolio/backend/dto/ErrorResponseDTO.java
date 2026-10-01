package com.portfolio.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response data returned when processing a request results in an error.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    /**
     * HTTP status code associated with the error.
     */
    private int status;

    /**
     * Human-readable description of the error.
     */
    private String message;
}
