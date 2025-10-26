package dev.kreaker.korporis.resource;

import dev.kreaker.korporis.model.Department;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @GET
    public List<Department> list() {
        return Department.listAll();
    }

    @POST
    @Transactional
    public Response create(Department department) {
        if (Department.findById(department.code) != null)
            return Response.status(Response.Status.CONFLICT)
                    .entity("Department with code " + department.code + " already exists.")
                    .build();

        department.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{code}")
    @Transactional
    public Response update(@PathParam("code") String code, Department department) {
        Department entity = Department.findById(code);
        if (entity == null) return Response.status(Response.Status.NOT_FOUND).build();

        entity.name = department.name;
        entity.description = department.description;
        entity.persist();
        return Response.ok().build();
    }

    @DELETE
    @Path("{code}")
    @Transactional
    public Response delete(@PathParam("code") String code) {
        Department entity = Department.findById(code);
        if (entity == null) return Response.status(Response.Status.NOT_FOUND).build();

        entity.delete();
        return Response.noContent().build();
    }
}
