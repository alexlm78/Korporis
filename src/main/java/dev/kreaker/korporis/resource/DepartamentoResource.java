package dev.kreaker.korporis.resource;

import dev.kreaker.korporis.model.Departamento;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/departamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartamentoResource {

   @GET
   public List<Departamento> list(){
      return Departamento.listAll();
   }

   @POST
   @Transactional
   public Response create(Departamento departamento){
      if(Departamento.findById(departamento.codigo) != null)
         return Response.status(Response.Status.CONFLICT).entity("Departamento con el código " + departamento.codigo + " ya existe.").build();

      departamento.persist();
      return Response.status(Response.Status.CREATED).build();
   }

   @PUT
   @Path("{codigo}")
   @Transactional
   public Response update(@PathParam("codigo") String codigo, Departamento departamento){
      Departamento entity = Departamento.findById(codigo);
      if(entity == null)
         return Response.status(Response.Status.NOT_FOUND).build();

      entity.nombre = departamento.nombre;
      entity.descripcion = departamento.descripcion;
      entity.persist();
      return Response.ok().build();
   }

   @DELETE
   @Path("{codigo}")
   @Transactional
   public Response delete(@PathParam("codigo") String codigo){
      //Departamento.deleteById(codigo);
      Departamento entity = Departamento.findById(codigo);
      if(entity == null)
         return Response.status(Response.Status.NOT_FOUND).build();

      entity.delete();
      return Response.noContent().build();
   }
}
