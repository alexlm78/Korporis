package dev.kreraker.korporis.dto;

import jakarta.validation.constraints.Size;

public class UpdateDepartmentRequest {

   @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
   public String code;

   @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
   public String name;

   @Size(max = 500, message = "Description cannot exceed 500 characters")
   public String description;

   @Size(max = 200, message = "Location cannot exceed 200 characters")
   public String location;

   public Long managerId;

   public Boolean active;

   /**
    * Optional ID of the new parent department.
    * Set to null explicitly via the dedicated endpoint to remove the parent.
    */
   public Long parentDepartmentId;
}
