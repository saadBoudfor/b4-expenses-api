package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class ResourceNotSavedException extends RuntimeException {
    @Getter
    private final String message;

    public ResourceNotSavedException(String message) {
        super();
        this.message = message;
    }
}
