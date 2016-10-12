package com.nibado.example.baseerrorhandler.service.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class User {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    public int getAge() {
        return dateOfBirth.until(LocalDate.now()).getYears();
    }
}
