package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.Department;

import java.time.LocalDateTime;

public class DepartmentDTO {

    public Long id;
    public String code;
    public String name;
    public String description;
    public String location;
    public Boolean active;
    public Long managerId;
    public String managerName;
    public Integer employeeCount;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static DepartmentDTO fromEntity(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTO dto = new DepartmentDTO();
        dto.id = department.id;
        dto.code = department.code;
        dto.name = department.name;
        dto.description = department.description;
        dto.location = department.location;
        dto.active = department.active;
        dto.createdAt = department.createdAt;
        dto.updatedAt = department.updatedAt;

        if (department.manager != null) {
            dto.managerId = department.manager.id;
            dto.managerName = department.manager.getFullName();
        }

        if (department.employees != null) {
            dto.employeeCount = department.employees.size();
        }

        return dto;
    }

    public static DepartmentDTO fromEntitySimple(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTO dto = new DepartmentDTO();
        dto.id = department.id;
        dto.code = department.code;
        dto.name = department.name;
        dto.description = department.description;
        dto.location = department.location;
        dto.active = department.active;
        dto.createdAt = department.createdAt;
        dto.updatedAt = department.updatedAt;
        return dto;
    }
}
