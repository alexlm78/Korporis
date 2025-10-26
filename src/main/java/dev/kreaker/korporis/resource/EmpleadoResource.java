package dev.kreaker.korporis.resource;

import dev.kreaker.korporis.model.Empleado;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
   public Response create(Empleado empleado){
      empleado.persist();
      empleado.updateCodigo();
      return Response.status(Response.Status.CREATED).entity(empleado).build();
   }

   @PUT
   @Path("{id}")
   @Transactional
   public Response update(@PathParam("id") Long id, Empleado empleado){
      Empleado entity = Empleado.findById(id);
      if(entity == null)
         return Response.status(Response.Status.NOT_FOUND).build();

      entity.nombres = empleado.nombres;
      entity.apellidos = empleado.apellidos;
      entity.fechaNacimiento = empleado.fechaNacimiento;
      entity.departamento = empleado.departamento;
      entity.persist();
      return Response.ok().build();
   }

   @DELETE
   @Path("{id}")
   @Transactional
   public Response delete(@PathParam("id") Long id){
      Empleado.deleteById(id);
      return Response.noContent().build();
   }
}
