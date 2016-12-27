package com.nibado.example.baseerrorhandler.lib;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

public abstract class BaseErrorHandler {
    private static final ExceptionMapping DEFAULT_ERROR = new ExceptionMapping(
            "SERVER_ERROR",
            "Internal server error",
            INTERNAL_SERVER_ERROR);

    private final Logger log;
    private final Map<Class, ExceptionMapping> exceptionMappings = new HashMap<>();

    public BaseErrorHandler(final Logger log) {
        this.log = log;

        registerMapping(MissingServletRequestParameterException.class, "MISSING_PARAMETER", "Missing request parameter", BAD_REQUEST);
        registerMapping(MethodArgumentTypeMismatchException.class, "ARGUMENT_TYPE_MISMATCH", "Argument type mismatch", BAD_REQUEST);
        registerMapping(HttpRequestMethodNotSupportedException.class, "METHOD_NOT_SUPPORTED", "HTTP method not supported", METHOD_NOT_ALLOWED);
        registerMapping(ServletRequestBindingException.class, "MISSING_HEADER", "Missing header in request", BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorDTO handleThrowable(final Throwable ex, final HttpServletResponse response) {
        ExceptionMapping mapping = exceptionMappings.getOrDefault(ex.getClass(), DEFAULT_ERROR);

        response.setStatus(mapping.status.value());

        return logAndConstruct(mapping, ex);
    }

    protected void registerMapping(final Class<?> clazz, final String code, final String message, final HttpStatus status) {
        exceptionMappings.put(clazz, new ExceptionMapping(code, message, status));
    }


    protected ErrorDTO construct(ExceptionMapping mapping) {
        return new ErrorDTO(mapping.code, mapping.message);
    }

    protected ErrorDTO logAndConstruct(ExceptionMapping mapping, Throwable t) {
        log.error("{} ({}): {}", mapping.message, mapping.code, t.getMessage(), t);

        return construct(mapping);
    }

    private static class ExceptionMapping {
        private final String message;
        private final String code;
        private final HttpStatus status;

        public ExceptionMapping(final String code, final String message, final HttpStatus status) {
            this.code = code;
            this.message = message;
            this.status = status;
        }
    }
}
