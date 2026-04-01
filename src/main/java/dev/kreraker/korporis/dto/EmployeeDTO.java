package dev.kreraker.korporis.dto;

import dev.kreraker.korporis.model.ContractType;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.model.EmployeeStatus;
import dev.kreraker.korporis.model.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeDTO {

   public Long id;
   public String employeeCode;
   public String firstName;
   public String lastName;
   public String fullName;
   public String dpi;
   public LocalDate birthDate;
   public Gender gender;
   public String email;
   public String phone;
   public String address;
   public LocalDate hireDate;
   public LocalDate terminationDate;
   public String position;
   public BigDecimal salary;
   public ContractType contractType;
   public EmployeeStatus status;
   public Long departmentId;
   public String departmentName;
   public String departmentCode;
   public Long supervisorId;
   public String supervisorName;
   public Integer subordinateCount;
   public LocalDateTime createdAt;
   public LocalDateTime updatedAt;

   public static EmployeeDTO fromEntity(Employee employee) {
      if (employee == null) {
         return null;
      }

      EmployeeDTO dto = new EmployeeDTO();
      dto.id = employee.id;
      dto.employeeCode = employee.employeeCode;
      dto.firstName = employee.firstName;
      dto.lastName = employee.lastName;
      dto.fullName = employee.getFullName();
      dto.dpi = employee.dpi;
      dto.birthDate = employee.birthDate;
      dto.gender = employee.gender;
      dto.email = employee.email;
      dto.phone = employee.phone;
      dto.address = employee.address;
      dto.hireDate = employee.hireDate;
      dto.terminationDate = employee.terminationDate;
      dto.position = employee.position;
      dto.salary = employee.salary;
      dto.contractType = employee.contractType;
      dto.status = employee.status;
      dto.createdAt = employee.createdAt;
      dto.updatedAt = employee.updatedAt;

      if (employee.department != null) {
         dto.departmentId = employee.department.id;
         dto.departmentName = employee.department.name;
         dto.departmentCode = employee.department.code;
      }

      if (employee.supervisor != null) {
         dto.supervisorId = employee.supervisor.id;
         dto.supervisorName = employee.supervisor.getFullName();
      }

      if (employee.subordinates != null) {
         dto.subordinateCount = employee.subordinates.size();
      }

      return dto;
   }

   public static EmployeeDTO fromEntitySimple(Employee employee) {
      if (employee == null) {
         return null;
      }

      EmployeeDTO dto = new EmployeeDTO();
      dto.id = employee.id;
      dto.employeeCode = employee.employeeCode;
      dto.firstName = employee.firstName;
      dto.lastName = employee.lastName;
      dto.fullName = employee.getFullName();
      dto.dpi = employee.dpi;
      dto.birthDate = employee.birthDate;
      dto.gender = employee.gender;
      dto.email = employee.email;
      dto.phone = employee.phone;
      dto.address = employee.address;
      dto.hireDate = employee.hireDate;
      dto.terminationDate = employee.terminationDate;
      dto.position = employee.position;
      dto.salary = employee.salary;
      dto.contractType = employee.contractType;
      dto.status = employee.status;
      dto.createdAt = employee.createdAt;
      dto.updatedAt = employee.updatedAt;
      return dto;
   }
}
