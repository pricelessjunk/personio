package com.personio.demo.domain.exceptions;

/**
 * This exception is thrown when a cycle is deducted in the structure
 */
public class CyclicStructureException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message the exception message
     */
    public CyclicStructureException(String message) {
        super(message);
    }
}
