package dev.kreraker.korporis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employees", indexes = {
   @Index(name = "idx_employee_code", columnList = "employee_code", unique = true),
   @Index(name = "idx_employee_dpi", columnList = "dpi", unique = true),
   @Index(name = "idx_employee_email", columnList = "email", unique = true),
   @Index(name = "idx_employee_department", columnList = "department_id"),
   @Index(name = "idx_employee_supervisor", columnList = "supervisor_id")
})
public class Employee {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id;

   @Column(name = "employee_code", nullable = false, unique = true, length = 20)
   public String employeeCode;

   @NotBlank(message = "First name is required")
   @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
   @Column(name = "first_name", nullable = false, length = 50)
   public String firstName;

   @NotBlank(message = "Last name is required")
   @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
   @Column(name = "last_name", nullable = false, length = 50)
   public String lastName;

   @NotBlank(message = "DPI is required")
   @Size(min = 13, max = 13, message = "DPI must be exactly 13 characters")
   @Pattern(regexp = "^[0-9]{13}$", message = "DPI must contain only 13 digits")
   @Column(nullable = false, unique = true, length = 13)
   public String dpi;

   @Past(message = "Birth date must be in the past")
   @Column(name = "birth_date")
   public LocalDate birthDate;

   @Enumerated(EnumType.STRING)
   @Column(length = 20)
   public Gender gender;

   @NotBlank(message = "Email is required")
   @Email(message = "Email must be valid")
   @Size(max = 100, message = "Email cannot exceed 100 characters")
   @Column(nullable = false, unique = true, length = 100)
   public String email;

   @Size(max = 20, message = "Phone number cannot exceed 20 characters")
   @Column(length = 20)
   public String phone;

   @Size(max = 300, message = "Address cannot exceed 300 characters")
   @Column(length = 300)
   public String address;

   @NotNull(message = "Hire date is required")
   @PastOrPresent(message = "Hire date cannot be in the future")
   @Column(name = "hire_date", nullable = false)
   public LocalDate hireDate;

   @Column(name = "termination_date")
   public LocalDate terminationDate;

   @NotBlank(message = "Position is required")
   @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
   @Column(nullable = false, length = 100)
   public String position;

   @NotNull(message = "Salary is required")
   @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
   @Digits(integer = 10, fraction = 2, message = "Salary must have at most 10 integer digits and 2 decimal places")
   @Column(nullable = false, precision = 12, scale = 2)
   public BigDecimal salary;

   @NotNull(message = "Contract type is required")
   @Enumerated(EnumType.STRING)
   @Column(name = "contract_type", nullable = false, length = 20)
   public ContractType contractType;

   @NotNull(message = "Employee status is required")
   @Enumerated(EnumType.STRING)
   @Column(nullable = false, length = 20)
   public EmployeeStatus status = EmployeeStatus.ACTIVE;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "department_id")
   public Department department;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "supervisor_id")
   public Employee supervisor;

   @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
   public List<Employee> subordinates = new ArrayList<>();

   @Column(name = "created_at", nullable = false, updatable = false)
   public LocalDateTime createdAt;

   @Column(name = "updated_at")
   public LocalDateTime updatedAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   public String getFullName() {
      return firstName + " " + lastName;
   }

   public boolean isActive() {
      return status == EmployeeStatus.ACTIVE;
   }

   public void addSubordinate(Employee subordinate) {
      subordinates.add(subordinate);
      subordinate.supervisor = this;
   }

   public void removeSubordinate(Employee subordinate) {
      subordinates.remove(subordinate);
      subordinate.supervisor = null;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Employee employee)) return false;
      return id != null && id.equals(employee.id);
   }

   @Override
   public int hashCode() {
      return getClass().hashCode();
   }

   @Override
   public String toString() {
      return "Employee{id=" + id + ", employeeCode='" + employeeCode + "', firstName='" + firstName
         + "', lastName='" + lastName + "', email='" + email + "', status=" + status + '}';
   }
}
