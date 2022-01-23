package com.personio.demo.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to wrap an error.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseData {
    private String message;
}
