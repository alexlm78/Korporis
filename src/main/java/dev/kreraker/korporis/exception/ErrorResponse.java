package dev.kreraker.korporis.exception;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Standard error response for REST API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
