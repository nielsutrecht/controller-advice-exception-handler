package com.nibado.example.baseerrorhandler.lib.exceptions;

import com.nibado.example.baseerrorhandler.lib.Error;

public abstract class BaseBadRequestException extends RuntimeException {
    private final Error error;

    public BaseBadRequestException(Error error, String message) {
        super(message);
        this.error = error;
    }

    public Error error() {
        return error;
    }
}
