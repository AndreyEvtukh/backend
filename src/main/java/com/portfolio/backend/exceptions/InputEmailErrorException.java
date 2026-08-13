package com.portfolio.backend.exceptions;

public class InputEmailErrorException extends RuntimeException {
    public InputEmailErrorException() {
        super("Email incorrect: ");
    }
}