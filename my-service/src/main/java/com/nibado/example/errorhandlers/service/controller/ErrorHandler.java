package com.nibado.example.errorhandlers.service.controller;

import com.nibado.example.errorhandlers.lib.BaseExceptionHandler;
import com.nibado.example.errorhandlers.service.controller.exception.NotImplementedException;
import com.nibado.example.errorhandlers.service.controller.exception.UserNotFoundException;
import com.nibado.example.errorhandlers.service.service.exception.InvalidSearchParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {
    public ErrorHandler() {
        super(log);

        registerMapping(UserNotFoundException.class, "USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND);
        registerMapping(NotImplementedException.class, "NOT_IMPLEMENTED", "Not implemented yet", HttpStatus.NOT_IMPLEMENTED);
        registerMapping(InvalidSearchParamException.class, "INVALID_PARAM", "Invalid search parameter", HttpStatus.BAD_REQUEST);
    }

}
