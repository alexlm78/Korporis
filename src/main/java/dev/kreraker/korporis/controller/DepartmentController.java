package dev.kreraker.korporis.controller;

import dev.kreraker.korporis.dto.CreateDepartmentRequest;
import dev.kreraker.korporis.dto.DepartmentDTO;
import dev.kreraker.korporis.dto.UpdateDepartmentRequest;
import dev.kreraker.korporis.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing departments.
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * GET /api/departments : Get all departments.
     * @param activeOnly if true, returns only active departments
     * @return list of departments
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.debug("REST request to get all departments, activeOnly: {}", activeOnly);
        List<DepartmentDTO> departments = activeOnly 
                ? departmentService.findAllActive() 
                : departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    /**
     * GET /api/departments/:id : Get department by ID.
     * @param id the department ID
     * @return the department
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        log.debug("REST request to get department by id: {}", id);
        DepartmentDTO department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    /**
     * GET /api/departments/code/:code : Get department by code.
     * @param code the department code
     * @return the department
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentDTO> getDepartmentByCode(@PathVariable String code) {
        log.debug("REST request to get department by code: {}", code);
        DepartmentDTO department = departmentService.findByCode(code);
        return ResponseEntity.ok(department);
    }

    /**
     * POST /api/departments : Create a new department.
     * @param request the create request
     * @return the created department
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        log.debug("REST request to create department: {}", request.getCode());
        DepartmentDTO created = departmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/departments/:id : Update an existing department.
     * @param id the department ID
     * @param request the update request
     * @return the updated department
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        log.debug("REST request to update department: {}", id);
        DepartmentDTO updated = departmentService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/departments/:id : Delete a department.
     * @param id the department ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete department: {}", id);
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/departments/:id/deactivate : Deactivate a department.
     * @param id the department ID
     * @return the deactivated department
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DepartmentDTO> deactivateDepartment(@PathVariable Long id) {
        log.debug("REST request to deactivate department: {}", id);
        DepartmentDTO deactivated = departmentService.deactivate(id);
        return ResponseEntity.ok(deactivated);
    }

    /**
     * PATCH /api/departments/:id/activate : Activate a department.
     * @param id the department ID
     * @return the activated department
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<DepartmentDTO> activateDepartment(@PathVariable Long id) {
        log.debug("REST request to activate department: {}", id);
        DepartmentDTO activated = departmentService.activate(id);
        return ResponseEntity.ok(activated);
    }

    /**
     * GET /api/departments/search : Search departments by name.
     * @param name the search term
     * @return list of matching departments
     */
    @GetMapping("/search")
    public ResponseEntity<List<DepartmentDTO>> searchDepartments(@RequestParam String name) {
        log.debug("REST request to search departments by name: {}", name);
        List<DepartmentDTO> departments = departmentService.searchByName(name);
        return ResponseEntity.ok(departments);
    }

    /**
     * GET /api/departments/:id/employee-count : Get employee count for a department.
     * @param id the department ID
     * @return the employee count
     */
    @GetMapping("/{id}/employee-count")
    public ResponseEntity<Long> getEmployeeCount(@PathVariable Long id) {
        log.debug("REST request to get employee count for department: {}", id);
        long count = departmentService.getEmployeeCount(id);
        return ResponseEntity.ok(count);
    }
}
