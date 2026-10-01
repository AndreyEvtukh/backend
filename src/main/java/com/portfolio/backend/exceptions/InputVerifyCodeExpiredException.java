package com.portfolio.backend.exceptions;

public class InputVerifyCodeExpiredException extends RuntimeException {
    public InputVerifyCodeExpiredException() {
        super("Code expired");
    }
}