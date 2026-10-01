package com.portfolio.backend.exceptions;

public class InputVerifyCodeUsedException extends RuntimeException {
    public InputVerifyCodeUsedException() {
        super("Code already used");
    }
}