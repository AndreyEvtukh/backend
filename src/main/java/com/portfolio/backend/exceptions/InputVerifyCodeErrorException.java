package com.portfolio.backend.exceptions;

public class InputVerifyCodeErrorException extends RuntimeException {
    public InputVerifyCodeErrorException() {
        super("Verify code is invalid");
    }
}