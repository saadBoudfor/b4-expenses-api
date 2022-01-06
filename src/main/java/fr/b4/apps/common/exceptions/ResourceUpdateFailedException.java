package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class ResourceUpdateFailedException extends RuntimeException {
    @Getter
    private final String message;

    public ResourceUpdateFailedException(String message) {
        super();
        this.message = message;
    }
}