package com.portfolio.backend.exceptions;

public class InputEmailErrorException extends RuntimeException {
    public InputEmailErrorException(String email) {
        super("Email " + email + " not registered");
    }
}