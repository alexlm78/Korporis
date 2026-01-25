package dev.kreraker.korporis.controller;

import dev.kreraker.korporis.dto.CreateEmployeeRequest;
import dev.kreraker.korporis.dto.EmployeeDTO;
import dev.kreraker.korporis.dto.UpdateEmployeeRequest;
import dev.kreraker.korporis.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing employees.
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * GET /api/employees : Get all employees.
     * @param activeOnly if true, returns only active employees
     * @return list of employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.debug("REST request to get all employees, activeOnly: {}", activeOnly);
        List<EmployeeDTO> employees = activeOnly 
                ? employeeService.findAllActive() 
                : employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/:id : Get employee by ID.
     * @param id the employee ID
     * @return the employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        log.debug("REST request to get employee by id: {}", id);
        EmployeeDTO employee = employeeService.findById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * GET /api/employees/code/:code : Get employee by employee code.
     * @param code the employee code
     * @return the employee
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(@PathVariable String code) {
        log.debug("REST request to get employee by code: {}", code);
        EmployeeDTO employee = employeeService.findByEmployeeCode(code);
        return ResponseEntity.ok(employee);
    }

    /**
     * POST /api/employees : Create a new employee.
     * @param request the create request
     * @return the created employee
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {
        log.debug("REST request to create employee: {} {}", request.getFirstName(), request.getLastName());
        EmployeeDTO created = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/employees/:id : Update an existing employee.
     * @param id the employee ID
     * @param request the update request
     * @return the updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        log.debug("REST request to update employee: {}", id);
        EmployeeDTO updated = employeeService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/employees/:id : Delete an employee.
     * @param id the employee ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete employee: {}", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/employees/:id/terminate : Terminate an employee.
     * @param id the employee ID
     * @return the terminated employee
     */
    @PatchMapping("/{id}/terminate")
    public ResponseEntity<EmployeeDTO> terminateEmployee(@PathVariable Long id) {
        log.debug("REST request to terminate employee: {}", id);
        EmployeeDTO terminated = employeeService.terminate(id);
        return ResponseEntity.ok(terminated);
    }

    /**
     * GET /api/employees/department/:departmentId : Get employees by department.
     * @param departmentId the department ID
     * @return list of employees in the department
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(
            @PathVariable Long departmentId) {
        log.debug("REST request to get employees by department: {}", departmentId);
        List<EmployeeDTO> employees = employeeService.findByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/:id/subordinates : Get subordinates of an employee.
     * @param id the supervisor's employee ID
     * @return list of subordinates
     */
    @GetMapping("/{id}/subordinates")
    public ResponseEntity<List<EmployeeDTO>> getSubordinates(@PathVariable Long id) {
        log.debug("REST request to get subordinates of employee: {}", id);
        List<EmployeeDTO> subordinates = employeeService.findSubordinates(id);
        return ResponseEntity.ok(subordinates);
    }

    /**
     * GET /api/employees/search : Search employees by name.
     * @param name the search term
     * @return list of matching employees
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam String name) {
        log.debug("REST request to search employees by name: {}", name);
        List<EmployeeDTO> employees = employeeService.searchByName(name);
        return ResponseEntity.ok(employees);
    }
}
