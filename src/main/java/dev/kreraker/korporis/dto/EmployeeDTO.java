package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Employee entity (response).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String employeeCode;
    
    // Personal Information
    private String firstName;
    private String lastName;
    private String fullName;
    private String dpi;
    private LocalDate birthDate;
    private Gender gender;
    
    // Contact Information
    private String email;
    private String phone;
    private String address;
    
    // Employment Information
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private String position;
    private BigDecimal salary;
    private ContractType contractType;
    private EmployeeStatus status;
    
    // Relationships
    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    private Long supervisorId;
    private String supervisorName;
    private Integer subordinateCount;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates an EmployeeDTO from an Employee entity.
     * @param employee the employee entity
     * @return the DTO
     */
    public static EmployeeDTO fromEntity(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTOBuilder builder = EmployeeDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFullName())
                .dpi(employee.getDpi())
                .birthDate(employee.getBirthDate())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .hireDate(employee.getHireDate())
                .terminationDate(employee.getTerminationDate())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .contractType(employee.getContractType())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt());

        if (employee.getDepartment() != null) {
            builder.departmentId(employee.getDepartment().getId())
                   .departmentName(employee.getDepartment().getName())
                   .departmentCode(employee.getDepartment().getCode());
        }

        if (employee.getSupervisor() != null) {
            builder.supervisorId(employee.getSupervisor().getId())
                   .supervisorName(employee.getSupervisor().getFullName());
        }

        if (employee.getSubordinates() != null) {
            builder.subordinateCount(employee.getSubordinates().size());
        }

        return builder.build();
    }

    /**
     * Creates a simple EmployeeDTO without relationships.
     * @param employee the employee entity
     * @return the DTO
     */
    public static EmployeeDTO fromEntitySimple(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFullName())
                .dpi(employee.getDpi())
                .birthDate(employee.getBirthDate())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .hireDate(employee.getHireDate())
                .terminationDate(employee.getTerminationDate())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .contractType(employee.getContractType())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
