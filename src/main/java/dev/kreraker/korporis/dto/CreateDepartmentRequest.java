package dev.kreraker.korporis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateDepartmentRequest {

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
    public String code;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    public String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    public String description;

    @Size(max = 200, message = "Location cannot exceed 200 characters")
    public String location;

    public Long managerId;

    /** Optional ID of the parent department. Null means this is a root department. */
    public Long parentDepartmentId;
}
