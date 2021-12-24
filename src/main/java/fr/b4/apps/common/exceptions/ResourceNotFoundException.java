package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {
    @Getter
    private final String message;

    public ResourceNotFoundException(String message) {
        super();
        this.message = message;
    }
}
