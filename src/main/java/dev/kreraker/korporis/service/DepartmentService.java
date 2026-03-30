package dev.kreraker.korporis.service;

import dev.kreraker.korporis.dto.CreateDepartmentRequest;
import dev.kreraker.korporis.dto.DepartmentDTO;
import dev.kreraker.korporis.dto.UpdateDepartmentRequest;
import dev.kreraker.korporis.exception.BusinessException;
import dev.kreraker.korporis.exception.DuplicateResourceException;
import dev.kreraker.korporis.exception.ResourceNotFoundException;
import dev.kreraker.korporis.model.Department;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.repository.DepartmentRepository;
import dev.kreraker.korporis.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentService {

    private static final Logger LOG = Logger.getLogger(DepartmentService.class);

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    EmployeeRepository employeeRepository;

    // -------------------------------------------------------------------------
    // Read operations
    // -------------------------------------------------------------------------

    public List<DepartmentDTO> findAll() {
        LOG.debug("Finding all departments");
        return departmentRepository.listAll().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> findAllActive() {
        LOG.debug("Finding all active departments");
        return departmentRepository.findByActiveTrue().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** Returns only root (top-level) departments — those without a parent. */
    public List<DepartmentDTO> findRootDepartments() {
        LOG.debug("Finding root departments");
        return departmentRepository.findRootDepartments().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** Returns only active root departments. */
    public List<DepartmentDTO> findActiveRootDepartments() {
        LOG.debug("Finding active root departments");
        return departmentRepository.findActiveRootDepartments().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DepartmentDTO findById(Long id) {
        LOG.debugf("Finding department by id: %d", id);
        Department department = departmentRepository.findByIdWithHierarchy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return DepartmentDTO.fromEntity(department);
    }

    public DepartmentDTO findByCode(String code) {
        LOG.debugf("Finding department by code: %s", code);
        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));
        return DepartmentDTO.fromEntity(department);
    }

    /** Returns the direct sub-departments of the given parent department. */
    public List<DepartmentDTO> findSubDepartments(Long parentId) {
        LOG.debugf("Finding sub-departments for parent id: %d", parentId);
        if (departmentRepository.findByIdOptional(parentId).isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", parentId);
        }
        return departmentRepository.findSubDepartmentsByParentId(parentId).stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** Returns the active direct sub-departments of the given parent department. */
    public List<DepartmentDTO> findActiveSubDepartments(Long parentId) {
        LOG.debugf("Finding active sub-departments for parent id: %d", parentId);
        if (departmentRepository.findByIdOptional(parentId).isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", parentId);
        }
        return departmentRepository.findActiveSubDepartmentsByParentId(parentId).stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Write operations
    // -------------------------------------------------------------------------

    @Transactional
    public DepartmentDTO create(CreateDepartmentRequest request) {
        LOG.debugf("Creating department with code: %s", request.code);

        if (departmentRepository.existsByCode(request.code)) {
            throw new DuplicateResourceException("Department", "code", request.code);
        }

        Department department = new Department();
        department.code = request.code.toUpperCase();
        department.name = request.name;
        department.description = request.description;
        department.location = request.location;
        department.active = true;

        if (request.managerId != null) {
            Employee manager = employeeRepository.findByIdOptional(request.managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.managerId));
            department.manager = manager;
        }

        if (request.parentDepartmentId != null) {
            Department parent = departmentRepository.findByIdOptional(request.parentDepartmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.parentDepartmentId));
            department.parentDepartment = parent;
        }

        departmentRepository.persist(department);
        LOG.infof("Created department with id: %d, code: %s, parentId: %s",
                department.id, department.code,
                department.parentDepartment != null ? department.parentDepartment.id : "none");
        return DepartmentDTO.fromEntity(department);
    }

    @Transactional
    public DepartmentDTO update(Long id, UpdateDepartmentRequest request) {
        LOG.debugf("Updating department with id: %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (request.code != null && !request.code.equalsIgnoreCase(department.code)) {
            if (departmentRepository.existsByCodeAndIdNot(request.code, id)) {
                throw new DuplicateResourceException("Department", "code", request.code);
            }
            department.code = request.code.toUpperCase();
        }

        if (request.name != null) {
            department.name = request.name;
        }
        if (request.description != null) {
            department.description = request.description;
        }
        if (request.location != null) {
            department.location = request.location;
        }
        if (request.active != null) {
            department.active = request.active;
        }

        if (request.managerId != null) {
            Employee manager = employeeRepository.findByIdOptional(request.managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.managerId));
            department.manager = manager;
        }

        if (request.parentDepartmentId != null) {
            validateAndSetParent(department, request.parentDepartmentId);
        }

        LOG.infof("Updated department with id: %d", department.id);
        return DepartmentDTO.fromEntity(department);
    }

    @Transactional
    public void delete(Long id) {
        LOG.debugf("Deleting department with id: %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Detach employees
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        for (Employee employee : employees) {
            employee.department = null;
        }

        // Detach sub-departments (promote them to root level)
        List<Department> subDepts = departmentRepository.findSubDepartmentsByParentId(id);
        for (Department sub : subDepts) {
            sub.parentDepartment = null;
        }

        departmentRepository.delete(department);
        LOG.infof("Deleted department with id: %d", id);
    }

    @Transactional
    public DepartmentDTO deactivate(Long id) {
        LOG.debugf("Deactivating department with id: %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.active = false;

        // Cascade deactivation to all direct sub-departments
        List<Department> subDepts = departmentRepository.findSubDepartmentsByParentId(id);
        for (Department sub : subDepts) {
            sub.active = false;
            LOG.infof("Cascade-deactivated sub-department id: %d (%s)", sub.id, sub.code);
        }

        LOG.infof("Deactivated department with id: %d", id);
        return DepartmentDTO.fromEntity(department);
    }

    @Transactional
    public DepartmentDTO activate(Long id) {
        LOG.debugf("Activating department with id: %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.active = true;
        LOG.infof("Activated department with id: %d", id);
        return DepartmentDTO.fromEntity(department);
    }

    public List<DepartmentDTO> searchByName(String name) {
        LOG.debugf("Searching departments by name: %s", name);
        return departmentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public long getEmployeeCount(Long id) {
        LOG.debugf("Getting employee count for department: %d", id);
        if (departmentRepository.findByIdOptional(id).isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        return departmentRepository.countEmployeesByDepartmentId(id);
    }

    public long getSubDepartmentCount(Long id) {
        LOG.debugf("Getting sub-department count for department: %d", id);
        if (departmentRepository.findByIdOptional(id).isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        return departmentRepository.countSubDepartmentsByParentId(id);
    }

    // -------------------------------------------------------------------------
    // Parent management endpoints
    // -------------------------------------------------------------------------

    /**
     * Assigns a parent department to the given department.
     * Validates that the assignment does not create a circular reference.
     */
    @Transactional
    public DepartmentDTO setParentDepartment(Long id, Long parentId) {
        LOG.debugf("Setting parent department %d for department %d", parentId, id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        validateAndSetParent(department, parentId);

        LOG.infof("Set parent department %d for department %d", parentId, id);
        return DepartmentDTO.fromEntity(department);
    }

    /**
     * Removes the parent department from the given department,
     * making it a root (top-level) department.
     */
    @Transactional
    public DepartmentDTO removeParentDepartment(Long id) {
        LOG.debugf("Removing parent department from department %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.parentDepartment = null;

        LOG.infof("Removed parent department from department %d", id);
        return DepartmentDTO.fromEntity(department);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Validates that assigning {@code parentId} as the parent of {@code department}
     * does not create a circular reference, then sets the parent.
     */
    private void validateAndSetParent(Department department, Long parentId) {
        if (department.id.equals(parentId)) {
            throw new BusinessException("A department cannot be its own parent.");
        }

        Department parent = departmentRepository.findByIdOptional(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", parentId));

        // Walk up the ancestor chain of the proposed parent to detect cycles
        Department ancestor = parent.parentDepartment;
        while (ancestor != null) {
            if (ancestor.id.equals(department.id)) {
                throw new BusinessException(
                        "Circular reference detected: department '" + department.code +
                        "' is already an ancestor of department '" + parent.code + "'.");
            }
            // Re-fetch to ensure the lazy proxy is resolved
            ancestor = departmentRepository.findByIdOptional(ancestor.id)
                    .map(d -> d.parentDepartment)
                    .orElse(null);
        }

        department.parentDepartment = parent;
    }
}
