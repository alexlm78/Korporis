package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.ContractType;
import dev.kreraker.korporis.model.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new employee.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {

    // Personal Information
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "DPI is required")
    @Size(min = 13, max = 13, message = "DPI must be exactly 13 characters")
    @Pattern(regexp = "^[0-9]{13}$", message = "DPI must contain only 13 digits")
    private String dpi;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private Gender gender;

    // Contact Information
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @Size(max = 300, message = "Address cannot exceed 300 characters")
    private String address;

    // Employment Information
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    private String position;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Salary must have at most 10 integer digits and 2 decimal places")
    private BigDecimal salary;

    @NotNull(message = "Contract type is required")
    private ContractType contractType;

    // Relationships
    private Long departmentId;

    private Long supervisorId;
}
