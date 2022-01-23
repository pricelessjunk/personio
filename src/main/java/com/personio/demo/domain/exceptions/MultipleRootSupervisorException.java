package com.personio.demo.domain.exceptions;

/**
 * This exception is thrown when more than one roots are detected
 */
public class MultipleRootSupervisorException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message the exception message
     */
    public MultipleRootSupervisorException(String message) {
        super(message);
    }
}
