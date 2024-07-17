package dev.kreaker.gestionEmp.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Empleado extends PanacheEntityBase {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id;
   
   public String codigo;
   
   @NotBlank
   public String nombres;
   
   @NotBlank
   public String apellidos;
   
   public String fechaNacimiento;
   
   @ManyToOne
   public Departamento departamento;
   
   public void prePersist() {
      if ( this.codigo == null ) {
         long count = Empleado.count();
         this.codigo = String.format("EMP-%04d", count + 1);
      }
   }
}
