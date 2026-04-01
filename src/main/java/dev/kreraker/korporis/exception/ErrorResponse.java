package dev.kreraker.korporis.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

   public LocalDateTime timestamp;
   public int status;
   public String error;
   public String message;
   public String path;
}
