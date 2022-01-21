package com.personio.demo.domain.exceptions;

/**
 * This exception is thrown when more than one roots are deteced
 */
public class MultipleRootSupervisorException extends Exception {
    /**
     * Constructor
     *
     * @param message the exception message
     */
    public MultipleRootSupervisorException(String message) {
        super(message);
    }
}
