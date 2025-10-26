package dev.kreaker.korporis.resource;

import dev.kreaker.korporis.model.Employee;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    @GET
    public List<Employee> list() {
        return Employee.listAll();
    }

    @POST
    @Transactional
    public Response create(Employee employee) {
        employee.persist();
        employee.updateCode();
        return Response.status(Response.Status.CREATED).entity(employee).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Employee employee) {
        Employee entity = Employee.findById(id);
        if (entity == null) return Response.status(Response.Status.NOT_FOUND).build();

        entity.firstName = employee.firstName;
        entity.lastName = employee.lastName;
        entity.birthDate = employee.birthDate;
        entity.department = employee.department;
        entity.persist();
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Employee.deleteById(id);
        return Response.noContent().build();
    }
}
