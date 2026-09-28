package com.donar.api.common.exception;

public class InvalidStateException extends RuntimeException {

    public InvalidStateException(String message) {
        super(message);
    }
}