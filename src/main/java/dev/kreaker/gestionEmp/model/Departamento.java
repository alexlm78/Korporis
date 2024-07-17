package dev.kreaker.gestionEmp.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Departamento extends PanacheEntityBase {
   @Id
   @NotBlank
   public String codigo;
   
   @NotBlank
   public String nombre;
   
   public String descripcion;
}
