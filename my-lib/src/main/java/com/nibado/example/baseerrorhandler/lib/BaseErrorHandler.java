package com.nibado.example.baseerrorhandler.lib;

import com.nibado.example.baseerrorhandler.lib.exceptions.BaseBadRequestException;
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
        return logAndConstruct(ErrorConstants.METHOD_NOT_SUPPORTED, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public ErrorDTO handleServletRequestBindingException(final ServletRequestBindingException ex) {
        return logAndConstruct(ErrorConstants.MISSING_HEADER, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ErrorDTO handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        return logAndConstruct(ErrorConstants.ARGUMENT_TYPE_MISMATCH, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ErrorDTO handleMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        return logAndConstruct(ErrorConstants.MISSING_REQUEST_PARAM, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseBadRequestException.class)
    @ResponseBody
    public ErrorDTO handleBaseBadRequestException(final BaseBadRequestException ex) {
        return logAndConstruct(ex.error(), ex);
    }

    protected void logThrowable(Throwable t) {
        log.error("{} occurred: {}", t.getClass().getSimpleName(), t.getMessage(), t);
    }

    protected ErrorDTO construct(Error error) {
        return new ErrorDTO(code(error), error.getMessage());
    }

    protected ErrorDTO logAndConstruct(Error error, Throwable t) {
        log.error("{} ({}): {}", error.getMessage(), code(error), t.getMessage(), t);

        return construct(error);
    }

    private String code(Error error) {
        return prefix + error.getCode();
    }
}
