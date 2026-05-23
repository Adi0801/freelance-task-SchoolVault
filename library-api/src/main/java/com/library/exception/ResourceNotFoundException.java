package com.library.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException.
     *
     * @param resourceName the name of the resource (e.g., "Book")
     * @param fieldName    the field used to search (e.g., "id")
     * @param fieldValue   the value of the field that was searched
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
}
