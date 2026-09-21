package com.donar.api.common.exception;

public record ErrorResponse(
        int status,
        String message
) {
}