package dev.kreaker.korporis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Departamento extends PanacheEntityBase {
   @Id
   @NotBlank(message="El código es requerido")
   @Size(max=50, message="El código no puede tener más de 50 caracteres")
   public String codigo;

   @NotBlank(message="El nombre es requerido")
   @Size(max=100, message="El nombre no puede tener más de 100 caracteres")
   public String nombre;

   public String descripcion;
}
