package com.library.exception;

/**
 * Exception thrown when an invalid business operation is attempted.
 */
public class InvalidOperationException extends RuntimeException {

    /**
     * Constructs a new InvalidOperationException.
     *
     * @param message the detail message describing the invalid operation
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}
