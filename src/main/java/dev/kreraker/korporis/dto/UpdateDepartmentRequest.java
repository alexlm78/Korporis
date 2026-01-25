package dev.kreraker.korporis.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for updating an existing department.
 * All fields are optional - only provided fields will be updated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDepartmentRequest {

    @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
    private String code;

    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;

    private Long managerId;

    private Boolean active;
}
