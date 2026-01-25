package dev.kreraker.korporis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating a new department.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDepartmentRequest {

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
    private String code;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;

    private Long managerId;
}
