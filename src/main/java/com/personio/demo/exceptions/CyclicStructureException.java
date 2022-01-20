package com.personio.demo.exceptions;

public class CyclicStructureException extends Exception {
    public CyclicStructureException(String message) {
        super(message);
    }
}
