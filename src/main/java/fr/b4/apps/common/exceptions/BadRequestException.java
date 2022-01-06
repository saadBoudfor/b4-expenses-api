package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class BadRequestException extends RuntimeException {
    @Getter
    private final String message;

    public BadRequestException(String message) {
        super();
        this.message = message;
    }
}