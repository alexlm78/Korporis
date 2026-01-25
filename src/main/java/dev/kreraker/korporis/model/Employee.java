package dev.kreraker.korporis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an employee in the organization.
 * Supports hierarchical structure with supervisor relationships.
 */
@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employee_code", columnList = "employee_code", unique = true),
    @Index(name = "idx_employee_dpi", columnList = "dpi", unique = true),
    @Index(name = "idx_employee_email", columnList = "email", unique = true),
    @Index(name = "idx_employee_department", columnList = "department_id"),
    @Index(name = "idx_employee_supervisor", columnList = "supervisor_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;

    // Personal Information
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "DPI is required")
    @Size(min = 13, max = 13, message = "DPI must be exactly 13 characters")
    @Pattern(regexp = "^[0-9]{13}$", message = "DPI must contain only 13 digits")
    @Column(nullable = false, unique = true, length = 13)
    private String dpi;

    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    // Contact Information
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(length = 20)
    private String phone;

    @Size(max = 300, message = "Address cannot exceed 300 characters")
    @Column(length = 300)
    private String address;

    // Employment Information
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String position;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Salary must have at most 10 integer digits and 2 decimal places")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @NotNull(message = "Contract type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false, length = 20)
    private ContractType contractType;

    @NotNull(message = "Employee status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Employee> subordinates = new ArrayList<>();

    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the full name of the employee.
     * @return concatenation of first name and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if the employee is currently active.
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return status == EmployeeStatus.ACTIVE;
    }

    /**
     * Adds a subordinate to this employee.
     * @param subordinate the employee to add as subordinate
     */
    public void addSubordinate(Employee subordinate) {
        subordinates.add(subordinate);
        subordinate.setSupervisor(this);
    }

    /**
     * Removes a subordinate from this employee.
     * @param subordinate the employee to remove as subordinate
     */
    public void removeSubordinate(Employee subordinate) {
        subordinates.remove(subordinate);
        subordinate.setSupervisor(null);
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
        return "Employee{" +
                "id=" + id +
                ", employeeCode='" + employeeCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", status=" + status +
                '}';
    }
}
