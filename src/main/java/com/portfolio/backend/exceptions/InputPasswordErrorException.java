package com.portfolio.backend.exceptions;

public class InputPasswordErrorException extends RuntimeException {
    public InputPasswordErrorException() {
        super("Wrong password.");
    }
}