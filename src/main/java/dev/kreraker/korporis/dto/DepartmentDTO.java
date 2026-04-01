package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.Department;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

   /**
    * ID of the parent department; null if this is a root department.
    */
   public Long parentDepartmentId;

   /**
    * Name of the parent department; null if this is a root department.
    */
   public String parentDepartmentName;

   /**
    * Direct sub-departments (shallow — no recursive nesting to avoid infinite loops).
    */
   public List<DepartmentDTO> subDepartments;

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

      if (department.parentDepartment != null) {
         dto.parentDepartmentId = department.parentDepartment.id;
         dto.parentDepartmentName = department.parentDepartment.name;
      }

      if (department.subDepartments != null && !department.subDepartments.isEmpty()) {
         dto.subDepartments = department.subDepartments.stream()
            .map(DepartmentDTO::fromEntitySimple)
            .collect(Collectors.toList());
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

      if (department.parentDepartment != null) {
         dto.parentDepartmentId = department.parentDepartment.id;
         dto.parentDepartmentName = department.parentDepartment.name;
      }

      return dto;
   }
}
