package com.nibado.example.baseerrorhandler.lib;

public enum ErrorConstants implements Error {
    GENERIC("900", "Internal server error"),
    MISSING_HEADER("901", "Missing header in request"),
    METHOD_NOT_SUPPORTED("902", "Method not supported"),
    ARGUMENT_TYPE_MISMATCH("903", "Argument type mismatch"),
    MISSING_REQUEST_PARAM("904", "Missing request parameter");

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
