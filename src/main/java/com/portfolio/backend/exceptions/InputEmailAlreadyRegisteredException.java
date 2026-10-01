package com.portfolio.backend.exceptions;

public class InputEmailAlreadyRegisteredException extends RuntimeException {
    public InputEmailAlreadyRegisteredException(String email) {
        super("Email " + email + " already registered");
    }
}