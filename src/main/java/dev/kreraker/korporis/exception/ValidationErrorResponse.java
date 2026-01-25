package dev.kreraker.korporis.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Error response for validation errors with field-level details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;
}
