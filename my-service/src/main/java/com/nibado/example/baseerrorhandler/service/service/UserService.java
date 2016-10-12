package com.nibado.example.baseerrorhandler.service.service;

import com.nibado.example.baseerrorhandler.service.controller.exception.InvalidSearchParamException;
import com.nibado.example.baseerrorhandler.service.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    private final Map<UUID, User> userDb;

    public UserService() {
        userDb = new HashMap<>();
    }

    private void add(User user) {
        userDb.put(user.getId(), user);
    }

    public Optional<User> get(UUID id) {
        return Optional.ofNullable(userDb.get(id));
    }

    public List<User> find(Integer age, Integer yearOfBirth, String firstName, String lastName, int limit) {
        Stream<User> s = userDb.values().stream();

        if(age != null) {
            if(age < 0) {
                throw InvalidSearchParamException.supplier("age", age).get();
            }
            s = s.filter(u -> u.getAge() == age);
        }

        if(yearOfBirth != null) {
            if(yearOfBirth < 1000 || yearOfBirth > LocalDate.now().getYear()) {
                throw InvalidSearchParamException.supplier("yearOfBirth", yearOfBirth).get();
            }
            s = s.filter(u -> u.getDateOfBirth().getYear() == yearOfBirth);
        }

        if(firstName != null) {
            s = s.filter(u -> u.getFirstName().equals(firstName));
        }

        if(lastName != null) {
            s = s.filter(u -> u.getLastName().equals(lastName));
        }

        return s.limit(limit).collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        add(new User(new UUID(0, 0), "John", "Johnson", LocalDate.of(1980, 2, 19)));
        add(new User(new UUID(0, 1), "Jill", "Jackson", LocalDate.of(1974, 7, 16)));
        add(new User(new UUID(0, 2), "Sam", "Jackson", LocalDate.of(2011, 10, 26)));
        add(new User(new UUID(0, 3), "Sophia", "Jackson", LocalDate.of(2014, 3, 25)));
    }
}
