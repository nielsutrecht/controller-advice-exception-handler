package com.nibado.example.baseerrorhandler.service.controller;

import com.nibado.example.baseerrorhandler.service.controller.exception.NotImplementedException;
import com.nibado.example.baseerrorhandler.service.service.domain.User;
import com.nibado.example.baseerrorhandler.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.nibado.example.baseerrorhandler.service.controller.exception.UserNotFoundException.supplier;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<User>> userMe(@RequestHeader("user-id") final UUID userId) {
        return get(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<User>> userId(@PathVariable final UUID userId) {
        return get(userId);
    }

    private Callable<ResponseEntity<User>> get(final UUID userId) {
        return () -> ResponseEntity.ok(userService
                .get(userId)
                .orElseThrow(supplier(userId))
        );
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<List<User>>> find(
            @RequestParam(name = "age", required = false) final Integer age,
            @RequestParam(name = "yearOfBirth", required = false) final Integer yearOfBirth,
            @RequestParam(name = "firstName", required = false) final String firstName,
            @RequestParam(name = "lastName", required = false) final String lastName,
            @RequestParam(name = "limit") final int limit
    ) {
        return () -> ResponseEntity.ok(userService.find(age, yearOfBirth, firstName, lastName, limit));
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Callable<ResponseEntity<Void>> userPost(@RequestBody final User user) {
        throw new NotImplementedException();
    }
}
