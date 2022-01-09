package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class ForbiddenException extends RuntimeException {
    @Getter
    private final String message;

    public ForbiddenException(String message) {
        super();
        this.message = message;
    }
}