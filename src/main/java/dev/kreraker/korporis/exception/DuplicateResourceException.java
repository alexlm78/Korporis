package dev.kreraker.korporis.exception;

public class DuplicateResourceException extends RuntimeException {

    public final String resourceName;
    public final String fieldName;
    public final Object fieldValue;

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
