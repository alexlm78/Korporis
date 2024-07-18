package dev.kreaker.gestion.resource;

import dev.kreaker.gestion.model.Empleado;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {
   
   @GET
   public List<Empleado> list(){
      return Empleado.listAll();
   }
   
   @POST
   @Transactional
   public void create(Empleado empleado){
      empleado.persist();
   }
   
   @PUT
   @Path("{id}")
   @Transactional
   public void update(@PathParam("id") Long id, Empleado empleado){
      Empleado entity = Empleado.findById(id);
      if(entity == null)
         throw new WebApplicationException("Empleado con el id " + id + " no existe.", 404);
      
      entity.nombres = empleado.nombres;
      entity.apellidos = empleado.apellidos;
      entity.fechaNacimiento = empleado.fechaNacimiento;
      entity.departamento = empleado.departamento;
      entity.persist();
   }
   
   @DELETE
   @Path("{id}")
   @Transactional
   public void delete(@PathParam("id") Long id){
      //Empleado.deleteById(id);
      Empleado entity = Empleado.findById(id);
      if(entity == null)
         throw new WebApplicationException("Empleado con el id " + id + " no existe.", 404);
      
      entity.delete();
   }
}

