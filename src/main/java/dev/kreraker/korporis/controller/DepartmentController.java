package dev.kreraker.korporis.controller;

import dev.kreraker.korporis.dto.CreateDepartmentRequest;
import dev.kreraker.korporis.dto.DepartmentDTO;
import dev.kreraker.korporis.dto.UpdateDepartmentRequest;
import dev.kreraker.korporis.service.DepartmentService;
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

@Path("/api/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController {

   private static final Logger LOG = Logger.getLogger(DepartmentController.class);

   @Inject
   DepartmentService departmentService;

   // -------------------------------------------------------------------------
   // Basic CRUD
   // -------------------------------------------------------------------------

   @GET
   public List<DepartmentDTO> getAllDepartments(
      @QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly,
      @QueryParam("rootOnly") @DefaultValue("false") boolean rootOnly) {
      LOG.debugf("REST request to get all departments, activeOnly: %s, rootOnly: %s", activeOnly, rootOnly);
      if (rootOnly) {
         return activeOnly
            ? departmentService.findActiveRootDepartments()
            : departmentService.findRootDepartments();
      }
      return activeOnly ? departmentService.findAllActive() : departmentService.findAll();
   }

   @GET
   @Path("/{id}")
   public DepartmentDTO getDepartmentById(@PathParam("id") Long id) {
      LOG.debugf("REST request to get department by id: %d", id);
      return departmentService.findById(id);
   }

   @GET
   @Path("/code/{code}")
   public DepartmentDTO getDepartmentByCode(@PathParam("code") String code) {
      LOG.debugf("REST request to get department by code: %s", code);
      return departmentService.findByCode(code);
   }

   @POST
   public Response createDepartment(@Valid CreateDepartmentRequest request) {
      LOG.debugf("REST request to create department: %s", request.code);
      DepartmentDTO created = departmentService.create(request);
      return Response.status(Response.Status.CREATED).entity(created).build();
   }

   @PUT
   @Path("/{id}")
   public DepartmentDTO updateDepartment(@PathParam("id") Long id, @Valid UpdateDepartmentRequest request) {
      LOG.debugf("REST request to update department: %d", id);
      return departmentService.update(id, request);
   }

   @DELETE
   @Path("/{id}")
   public Response deleteDepartment(@PathParam("id") Long id) {
      LOG.debugf("REST request to delete department: %d", id);
      departmentService.delete(id);
      return Response.noContent().build();
   }

   @PATCH
   @Path("/{id}/deactivate")
   public DepartmentDTO deactivateDepartment(@PathParam("id") Long id) {
      LOG.debugf("REST request to deactivate department: %d", id);
      return departmentService.deactivate(id);
   }

   @PATCH
   @Path("/{id}/activate")
   public DepartmentDTO activateDepartment(@PathParam("id") Long id) {
      LOG.debugf("REST request to activate department: %d", id);
      return departmentService.activate(id);
   }

   @GET
   @Path("/search")
   public List<DepartmentDTO> searchDepartments(@QueryParam("name") String name) {
      LOG.debugf("REST request to search departments by name: %s", name);
      return departmentService.searchByName(name);
   }

   @GET
   @Path("/{id}/employee-count")
   public long getEmployeeCount(@PathParam("id") Long id) {
      LOG.debugf("REST request to get employee count for department: %d", id);
      return departmentService.getEmployeeCount(id);
   }

   // -------------------------------------------------------------------------
   // Sub-department endpoints
   // -------------------------------------------------------------------------

   /**
    * GET /api/departments/{id}/sub-departments
    * Returns the direct sub-departments of the given department.
    * Use ?activeOnly=true to filter only active ones.
    */
   @GET
   @Path("/{id}/sub-departments")
   public List<DepartmentDTO> getSubDepartments(
      @PathParam("id") Long id,
      @QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly) {
      LOG.debugf("REST request to get sub-departments for department: %d, activeOnly: %s", id.toString(), activeOnly);
      return activeOnly
         ? departmentService.findActiveSubDepartments(id)
         : departmentService.findSubDepartments(id);
   }

   /**
    * GET /api/departments/{id}/sub-departments/count
    * Returns the number of direct sub-departments.
    */
   @GET
   @Path("/{id}/sub-departments/count")
   public long getSubDepartmentCount(@PathParam("id") Long id) {
      LOG.debugf("REST request to get sub-department count for department: %d", id);
      return departmentService.getSubDepartmentCount(id);
   }

   /**
    * PUT /api/departments/{id}/parent/{parentId}
    * Assigns a parent department to the given department.
    * Returns 409 if the assignment would create a circular reference.
    */
   @PUT
   @Path("/{id}/parent/{parentId}")
   public DepartmentDTO setParentDepartment(
      @PathParam("id") Long id,
      @PathParam("parentId") Long parentId) {
      LOG.debugf("REST request to set parent %d for department %d", parentId, id);
      return departmentService.setParentDepartment(id, parentId);
   }

   /**
    * DELETE /api/departments/{id}/parent
    * Removes the parent from the given department, making it a root department.
    */
   @DELETE
   @Path("/{id}/parent")
   public DepartmentDTO removeParentDepartment(@PathParam("id") Long id) {
      LOG.debugf("REST request to remove parent from department %d", id);
      return departmentService.removeParentDepartment(id);
   }
}
