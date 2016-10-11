package com.nibado.example.baseerrorhandler.service;

import com.nibado.example.baseerrorhandler.lib.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@Slf4j
public class ErrorController {
    @RequestMapping(value = "/error/{error}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<ErrorDTO>> error(@PathVariable final String error) {
        return () -> {
            log.info("Error: {}", error);

            switch(error) {
                case "illegalArgument":
                    throw new IllegalArgumentException("Foo!");
                case "userNotFound":
                    throw new UserNotFoundException();
            }

            return ResponseEntity.ok(new ErrorDTO("NO", "ERROR"));
        };
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<ErrorDTO>> errorMe(@RequestHeader("user-id") final UUID userId) {
        return () -> ResponseEntity.ok(new ErrorDTO("NO", "ERROR"));
    }

    @RequestMapping(value = "/number/{num}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<ErrorDTO>> errorNum(@PathVariable final int num) {
        return () -> ResponseEntity.ok(new ErrorDTO("NO", "ERROR"));
    }

    @RequestMapping(value = "/param", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<ErrorDTO>> errorParam(
            @RequestParam(name = "number1") final int number1,
            @RequestParam(name = "number2", required = false) final Integer number2) {
        return () -> ResponseEntity.ok(new ErrorDTO("NO", "ERROR"));
    }
}
