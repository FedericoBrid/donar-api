package com.donar.api.common.exception;

public class InactiveUserException extends RuntimeException{
    public InactiveUserException(String message) {
        super(message);
    }
}
