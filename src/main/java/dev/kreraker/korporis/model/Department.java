package dev.kreraker.korporis.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments", indexes = {
   @Index(name = "idx_department_code", columnList = "code", unique = true),
   @Index(name = "idx_department_parent", columnList = "parent_department_id")
})
public class Department {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id;

   @NotBlank(message = "Department code is required")
   @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
   @Column(nullable = false, unique = true, length = 10)
   public String code;

   @NotBlank(message = "Department name is required")
   @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
   @Column(nullable = false, length = 100)
   public String name;

   @Size(max = 500, message = "Description cannot exceed 500 characters")
   @Column(length = 500)
   public String description;

   @Size(max = 200, message = "Location cannot exceed 200 characters")
   @Column(length = 200)
   public String location;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "manager_id")
   public Employee manager;

   @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
   public List<Employee> employees = new ArrayList<>();

   /**
    * Parent department — null means this is a root/top-level department.
    */
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "parent_department_id")
   public Department parentDepartment;

   /**
    * Direct sub-departments that belong to this department.
    */
   @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL)
   public List<Department> subDepartments = new ArrayList<>();

   @Column(nullable = false)
   public Boolean active = true;

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

   public void addEmployee(Employee employee) {
      employees.add(employee);
      employee.department = this;
   }

   public void removeEmployee(Employee employee) {
      employees.remove(employee);
      employee.department = null;
   }

   public void addSubDepartment(Department subDepartment) {
      subDepartments.add(subDepartment);
      subDepartment.parentDepartment = this;
   }

   public void removeSubDepartment(Department subDepartment) {
      subDepartments.remove(subDepartment);
      subDepartment.parentDepartment = null;
   }

   /**
    * Returns true if this department is a root (top-level) department.
    */
   public boolean isRootDepartment() {
      return parentDepartment == null;
   }

   /**
    * Returns true if this department has sub-departments.
    */
   public boolean hasSubDepartments() {
      return subDepartments != null && !subDepartments.isEmpty();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Department department)) return false;
      return id != null && id.equals(department.id);
   }

   @Override
   public int hashCode() {
      return getClass().hashCode();
   }

   @Override
   public String toString() {
      return "Department{id=" + id + ", code='" + code + "', name='" + name + "', active=" + active +
         ", parentId=" + (parentDepartment != null ? parentDepartment.id : null) + '}';
   }
}
