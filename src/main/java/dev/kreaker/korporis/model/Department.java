package dev.kreaker.korporis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Department extends PanacheEntityBase {
    @Id
    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code cannot be longer than 50 characters")
    public String code;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    public String name;

    public String description;
}
