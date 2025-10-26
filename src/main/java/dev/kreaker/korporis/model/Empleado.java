package dev.kreaker.korporis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Empleado extends PanacheEntityBase {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id;

   public String codigo;

   @NotBlank(message="Los nombres son requeridos")
   @Size(max=100, message="Los nombres no pueden tener más de 100 caracteres")
   public String nombres;

   @NotBlank(message="Los apellidos son requeridos")
   @Size(max=100, message="Los apellidos no pueden tener más de 100 caracteres")
   public String apellidos;

   public String fechaNacimiento;

   @NotNull(message="El departamento es requerido")
   @ManyToOne
   public Departamento departamento;

   @PostPersist
   public void updateCodigo() {
      if(this.codigo == null) {
         this.codigo = String.format("EMP-%04d", this.id);
         Empleado.persist(this);
      }
   }
}
