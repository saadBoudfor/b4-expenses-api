package fr.b4.apps.common.exceptions;

import lombok.Getter;

public class ThirdPartyException extends RuntimeException {
    @Getter
    private final String message;

    public ThirdPartyException(String message) {
        super();
        this.message = message;
    }
}