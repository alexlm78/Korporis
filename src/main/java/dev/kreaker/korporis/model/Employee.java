package dev.kreaker.korporis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Employee extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String code;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot be longer than 100 characters")
    public String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot be longer than 100 characters")
    public String lastName;

    public String birthDate;

    @NotNull(message = "Department is required")
    @ManyToOne
    public Department department;

    @PostPersist
    public void updateCode() {
        if (this.code == null) {
            this.code = String.format("EMP-%04d", this.id);
            Employee.persist(this);
        }
    }
}
