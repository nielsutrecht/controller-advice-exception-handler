package com.nibado.example.baseerrorhandler.service.service.exception;

import com.nibado.example.baseerrorhandler.lib.exceptions.BaseBadRequestException;
import com.nibado.example.baseerrorhandler.service.controller.ErrorConstants;

import java.util.function.Supplier;

public class InvalidSearchParamException extends BaseBadRequestException {
    public InvalidSearchParamException(String message) {
        super(ErrorConstants.INVALID_SEARCH_PARAMETER, message);
    }

    public static Supplier<InvalidSearchParamException> supplier(final String param, final Object value) {
        return () -> new InvalidSearchParamException(String.format("Value '%s' is not valid for parameter '%s'", value, param));
    }
}
