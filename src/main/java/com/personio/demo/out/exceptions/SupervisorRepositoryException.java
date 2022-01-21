package com.personio.demo.out.exceptions;

import com.personio.demo.out.repositories.SupervisorRepository;

/**
 * An exception that represents an error in the {@link SupervisorRepository}
 */
public class SupervisorRepositoryException extends Exception {
    /**
     * Constructor
     *
     * @param message the exception message
     */
    public SupervisorRepositoryException(String message) {
        super(message);
    }
}
