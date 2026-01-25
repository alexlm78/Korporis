package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.Department;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Department entity (response).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String location;
    private Boolean active;
    private Long managerId;
    private String managerName;
    private Integer employeeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a DepartmentDTO from a Department entity.
     * @param department the department entity
     * @return the DTO
     */
    public static DepartmentDTO fromEntity(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTOBuilder builder = DepartmentDTO.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .description(department.getDescription())
                .location(department.getLocation())
                .active(department.getActive())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt());

        if (department.getManager() != null) {
            builder.managerId(department.getManager().getId())
                   .managerName(department.getManager().getFullName());
        }

        if (department.getEmployees() != null) {
            builder.employeeCount(department.getEmployees().size());
        }

        return builder.build();
    }

    /**
     * Creates a simple DepartmentDTO without relationships.
     * @param department the department entity
     * @return the DTO
     */
    public static DepartmentDTO fromEntitySimple(Department department) {
        if (department == null) {
            return null;
        }

        return DepartmentDTO.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .description(department.getDescription())
                .location(department.getLocation())
                .active(department.getActive())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
