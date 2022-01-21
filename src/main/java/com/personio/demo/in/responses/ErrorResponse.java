package com.personio.demo.in.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to wrap an error.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
