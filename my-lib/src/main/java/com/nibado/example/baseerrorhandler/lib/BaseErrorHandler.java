package com.nibado.example.baseerrorhandler.lib;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public abstract class BaseErrorHandler {
    private final String prefix;
    private final Logger log;

    public BaseErrorHandler(final String prefix, final Logger log) {
        this.prefix = prefix;
        this.log = log;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorDTO handleThrowable(final Throwable ex) {
        logThrowable(ex);
        return construct(ErrorConstants.GENERIC);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ErrorDTO handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException ex) {
        logThrowable(ex);
        return construct(ErrorConstants.METHOD_NOT_SUPPORTED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public ErrorDTO handleServletRequestBindingException(final ServletRequestBindingException ex) {
        logThrowable(ex);
        return construct(ErrorConstants.MISSING_HEADER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ErrorDTO handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        logThrowable(ex);
        return construct(ErrorConstants.ARGUMENT_TYPE_MISMATCH);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ErrorDTO handleMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        logThrowable(ex);
        return construct(ErrorConstants.MISSING_REQUEST_PARAM);
    }

    protected void logThrowable(Throwable t) {
        log.error("{} occurred: {}", t.getClass().getSimpleName(), t.getMessage(), t);
    }

    protected ErrorDTO construct(Error error) {
        return new ErrorDTO(prefix + error.getCode(), error.getMessage());
    }
}
