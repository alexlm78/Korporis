package dev.kreaker.gestion.resource;

import dev.kreaker.gestion.model.Departamento;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
   public void create(Departamento departamento){
      departamento.persist();
   }
   
   @PUT
   @Path("{codigo}")
   @Transactional
   public void update(@PathParam("codigo") String codigo, Departamento departamento){
      Departamento entity = Departamento.findById(codigo);
      if(entity == null)
         throw new WebApplicationException("Departamento con el código " + codigo + " no existe.", 404);
      
      entity.nombre = departamento.nombre;
      entity.descripcion = departamento.descripcion;
      entity.persist();
   }
   
   @DELETE
   @Path("{codigo}")
   @Transactional
   public void delete(@PathParam("codigo") String codigo){
      //Departamento.deleteById(codigo);
      Departamento entity = Departamento.findById(codigo);
      if(entity == null)
         throw new WebApplicationException("Departamento con el código " + codigo + " no existe.", 404);
      
      entity.delete();
   }
}
