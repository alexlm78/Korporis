package dev.kreraker.korporis.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse {

   public LocalDateTime timestamp;
   public int status;
   public String error;
   public String message;
   public String path;
   public Map<String, String> fieldErrors;
}
