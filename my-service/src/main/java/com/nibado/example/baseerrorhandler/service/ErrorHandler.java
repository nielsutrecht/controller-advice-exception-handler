package com.nibado.example.baseerrorhandler.service;

import com.nibado.example.baseerrorhandler.lib.BaseErrorHandler;
import com.nibado.example.baseerrorhandler.lib.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends BaseErrorHandler {
    public ErrorHandler() {
        super("MY", log);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ErrorDTO handleUserNotFoundException(final UserNotFoundException ex) {
        logThrowable(ex);
        return construct(ErrorConstants.USER_NOT_FOUND);
    }
}
