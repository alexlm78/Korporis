package dev.kreraker.korporis.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler {

   private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

   @Context
   UriInfo uriInfo;

   @Provider
   public static class ResourceNotFoundMapper implements ExceptionMapper<ResourceNotFoundException> {

      @Context
      UriInfo uriInfo;

      @Override
      public Response toResponse(ResourceNotFoundException ex) {
         LOG.errorf("Resource not found: %s", ex.getMessage());

         ErrorResponse error = new ErrorResponse();
         error.timestamp = LocalDateTime.now();
         error.status = 404;
         error.error = "Not Found";
         error.message = ex.getMessage();
         error.path = uriInfo.getPath();

         return Response.status(Response.Status.NOT_FOUND).entity(error).build();
      }
   }

   @Provider
   public static class DuplicateResourceMapper implements ExceptionMapper<DuplicateResourceException> {

      @Context
      UriInfo uriInfo;

      @Override
      public Response toResponse(DuplicateResourceException ex) {
         LOG.errorf("Duplicate resource: %s", ex.getMessage());

         ErrorResponse error = new ErrorResponse();
         error.timestamp = LocalDateTime.now();
         error.status = 409;
         error.error = "Conflict";
         error.message = ex.getMessage();
         error.path = uriInfo.getPath();

         return Response.status(Response.Status.CONFLICT).entity(error).build();
      }
   }

   @Provider
   public static class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

      @Context
      UriInfo uriInfo;

      @Override
      public Response toResponse(BusinessException ex) {
         LOG.errorf("Business error: %s", ex.getMessage());

         ErrorResponse error = new ErrorResponse();
         error.timestamp = LocalDateTime.now();
         error.status = 400;
         error.error = "Bad Request";
         error.message = ex.getMessage();
         error.path = uriInfo.getPath();

         return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
      }
   }

   @Provider
   public static class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

      @Context
      UriInfo uriInfo;

      @Override
      public Response toResponse(ConstraintViolationException ex) {
         LOG.errorf("Validation error: %s", ex.getMessage());

         Map<String, String> fieldErrors = new HashMap<>();
         for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            // Extract the field name from the full path (e.g., "create.arg0.firstName" -> "firstName")
            if (field.contains(".")) {
               field = field.substring(field.lastIndexOf('.') + 1);
            }
            fieldErrors.put(field, violation.getMessage());
         }

         ValidationErrorResponse error = new ValidationErrorResponse();
         error.timestamp = LocalDateTime.now();
         error.status = 400;
         error.error = "Validation Failed";
         error.message = "One or more fields have validation errors";
         error.path = uriInfo.getPath();
         error.fieldErrors = fieldErrors;

         return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
      }
   }
}
