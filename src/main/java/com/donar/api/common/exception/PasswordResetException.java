package com.donar.api.common.exception;

public class PasswordResetException extends RuntimeException {

    public PasswordResetException(String message) {
        super(message);
    }
}