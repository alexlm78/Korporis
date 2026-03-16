package dev.kreraker.korporis.exception;

public class ResourceNotFoundException extends RuntimeException {

    public final String resourceName;
    public final String fieldName;
    public final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
