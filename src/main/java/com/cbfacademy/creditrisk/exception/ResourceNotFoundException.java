package com.cbfacademy.creditrisk.exception;

/**
 * Custom exception for resources that cannot be found.
 * Typically used in service or controller layers when a requested entity does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
