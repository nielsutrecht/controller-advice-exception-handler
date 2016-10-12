package com.nibado.example.baseerrorhandler.service.controller.exception;

import java.util.UUID;
import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String message) {
        super(message);
    }

    public static Supplier<UserNotFoundException> supplier(final UUID id) {
        return () -> new UserNotFoundException(String.format("User with id '%s' not found", id));
    }
}
