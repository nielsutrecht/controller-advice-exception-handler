package com.nibado.example.errorhandlers.lib;

import lombok.Data;

@Data
public class ErrorDTO {
    private final String code;
    private final String message;
}
