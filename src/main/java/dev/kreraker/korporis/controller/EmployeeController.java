package dev.kreraker.korporis.controller;

import dev.kreraker.korporis.dto.CreateEmployeeRequest;
import dev.kreraker.korporis.dto.EmployeeDTO;
import dev.kreraker.korporis.dto.UpdateEmployeeRequest;
import dev.kreraker.korporis.service.EmployeeService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {

    private static final Logger LOG = Logger.getLogger(EmployeeController.class);

    @Inject
    EmployeeService employeeService;

    @GET
    public List<EmployeeDTO> getAllEmployees(
            @QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly) {
        LOG.debugf("REST request to get all employees, activeOnly: %s", activeOnly);
        return activeOnly ? employeeService.findAllActive() : employeeService.findAll();
    }

    @GET
    @Path("/{id}")
    public EmployeeDTO getEmployeeById(@PathParam("id") Long id) {
        LOG.debugf("REST request to get employee by id: %d", id);
        return employeeService.findById(id);
    }

    @GET
    @Path("/code/{code}")
    public EmployeeDTO getEmployeeByCode(@PathParam("code") String code) {
        LOG.debugf("REST request to get employee by code: %s", code);
        return employeeService.findByEmployeeCode(code);
    }

    @POST
    public Response createEmployee(@Valid CreateEmployeeRequest request) {
        LOG.debugf("REST request to create employee: %s %s", request.firstName, request.lastName);
        EmployeeDTO created = employeeService.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public EmployeeDTO updateEmployee(@PathParam("id") Long id, @Valid UpdateEmployeeRequest request) {
        LOG.debugf("REST request to update employee: %d", id);
        return employeeService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") Long id) {
        LOG.debugf("REST request to delete employee: %d", id);
        employeeService.delete(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/terminate")
    public EmployeeDTO terminateEmployee(@PathParam("id") Long id) {
        LOG.debugf("REST request to terminate employee: %d", id);
        return employeeService.terminate(id);
    }

    @GET
    @Path("/department/{departmentId}")
    public List<EmployeeDTO> getEmployeesByDepartment(@PathParam("departmentId") Long departmentId) {
        LOG.debugf("REST request to get employees by department: %d", departmentId);
        return employeeService.findByDepartment(departmentId);
    }

    @GET
    @Path("/{id}/subordinates")
    public List<EmployeeDTO> getSubordinates(@PathParam("id") Long id) {
        LOG.debugf("REST request to get subordinates of employee: %d", id);
        return employeeService.findSubordinates(id);
    }

    @GET
    @Path("/search")
    public List<EmployeeDTO> searchEmployees(@QueryParam("name") String name) {
        LOG.debugf("REST request to search employees by name: %s", name);
        return employeeService.searchByName(name);
    }
}
