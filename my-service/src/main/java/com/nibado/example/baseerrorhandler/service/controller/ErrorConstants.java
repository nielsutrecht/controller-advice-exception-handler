package com.nibado.example.baseerrorhandler.service.controller;

import com.nibado.example.baseerrorhandler.lib.Error;

public enum ErrorConstants implements Error {
    USER_NOT_FOUND("001", "User not found"),
    INVALID_SEARCH_PARAMETER("002", "Invalid search parameter");

    private final String code;
    private final String message;

    ErrorConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
