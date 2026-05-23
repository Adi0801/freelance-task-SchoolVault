package com.library.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException.
     *
     * @param resourceName the name of the resource (e.g., "Book")
     * @param fieldName    the field that has a duplicate value (e.g., "isbn")
     * @param fieldValue   the duplicate value
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
    }
}
