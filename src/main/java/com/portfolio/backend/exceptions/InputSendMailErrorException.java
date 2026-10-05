package com.portfolio.backend.exceptions;

public class InputSendMailErrorException extends RuntimeException {
    public InputSendMailErrorException() {
        super("Email send failed");
    }
}