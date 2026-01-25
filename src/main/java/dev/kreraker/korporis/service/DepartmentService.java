package dev.kreraker.korporis.service;

import dev.kreraker.korporis.dto.CreateDepartmentRequest;
import dev.kreraker.korporis.dto.DepartmentDTO;
import dev.kreraker.korporis.dto.UpdateDepartmentRequest;
import dev.kreraker.korporis.exception.DuplicateResourceException;
import dev.kreraker.korporis.exception.ResourceNotFoundException;
import dev.kreraker.korporis.model.Department;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.repository.DepartmentRepository;
import dev.kreraker.korporis.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing departments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Retrieves all departments.
     * @return list of all departments
     */
    public List<DepartmentDTO> findAll() {
        log.debug("Finding all departments");
        return departmentRepository.findAll().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all active departments.
     * @return list of active departments
     */
    public List<DepartmentDTO> findAllActive() {
        log.debug("Finding all active departments");
        return departmentRepository.findByActiveTrue().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a department by its ID.
     * @param id the department ID
     * @return the department DTO
     * @throws ResourceNotFoundException if department not found
     */
    public DepartmentDTO findById(Long id) {
        log.debug("Finding department by id: {}", id);
        Department department = departmentRepository.findByIdWithManager(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return DepartmentDTO.fromEntity(department);
    }

    /**
     * Retrieves a department by its code.
     * @param code the department code
     * @return the department DTO
     * @throws ResourceNotFoundException if department not found
     */
    public DepartmentDTO findByCode(String code) {
        log.debug("Finding department by code: {}", code);
        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));
        return DepartmentDTO.fromEntity(department);
    }

    /**
     * Creates a new department.
     * @param request the create request
     * @return the created department DTO
     * @throws DuplicateResourceException if department code already exists
     */
    @Transactional
    public DepartmentDTO create(CreateDepartmentRequest request) {
        log.debug("Creating department with code: {}", request.getCode());

        // Check for duplicate code
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Department", "code", request.getCode());
        }

        Department department = Department.builder()
                .code(request.getCode().toUpperCase())
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .active(true)
                .build();

        // Set manager if provided
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getManagerId()));
            department.setManager(manager);
        }

        Department saved = departmentRepository.save(department);
        log.info("Created department with id: {} and code: {}", saved.getId(), saved.getCode());
        return DepartmentDTO.fromEntity(saved);
    }

    /**
     * Updates an existing department.
     * @param id the department ID
     * @param request the update request
     * @return the updated department DTO
     * @throws ResourceNotFoundException if department not found
     * @throws DuplicateResourceException if new code already exists
     */
    @Transactional
    public DepartmentDTO update(Long id, UpdateDepartmentRequest request) {
        log.debug("Updating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Check for duplicate code if code is being changed
        if (request.getCode() != null && !request.getCode().equalsIgnoreCase(department.getCode())) {
            if (departmentRepository.existsByCodeAndIdNot(request.getCode(), id)) {
                throw new DuplicateResourceException("Department", "code", request.getCode());
            }
            department.setCode(request.getCode().toUpperCase());
        }

        if (request.getName() != null) {
            department.setName(request.getName());
        }

        if (request.getDescription() != null) {
            department.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            department.setLocation(request.getLocation());
        }

        if (request.getActive() != null) {
            department.setActive(request.getActive());
        }

        // Update manager if provided
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getManagerId()));
            department.setManager(manager);
        }

        Department saved = departmentRepository.save(department);
        log.info("Updated department with id: {}", saved.getId());
        return DepartmentDTO.fromEntity(saved);
    }

    /**
     * Deletes a department by its ID.
     * @param id the department ID
     * @throws ResourceNotFoundException if department not found
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Remove department reference from all employees
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        for (Employee employee : employees) {
            employee.setDepartment(null);
        }
        employeeRepository.saveAll(employees);

        departmentRepository.delete(department);
        log.info("Deleted department with id: {}", id);
    }

    /**
     * Deactivates a department (soft delete).
     * @param id the department ID
     * @return the deactivated department DTO
     * @throws ResourceNotFoundException if department not found
     */
    @Transactional
    public DepartmentDTO deactivate(Long id) {
        log.debug("Deactivating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setActive(false);
        Department saved = departmentRepository.save(department);
        log.info("Deactivated department with id: {}", id);
        return DepartmentDTO.fromEntity(saved);
    }

    /**
     * Activates a department.
     * @param id the department ID
     * @return the activated department DTO
     * @throws ResourceNotFoundException if department not found
     */
    @Transactional
    public DepartmentDTO activate(Long id) {
        log.debug("Activating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setActive(true);
        Department saved = departmentRepository.save(department);
        log.info("Activated department with id: {}", id);
        return DepartmentDTO.fromEntity(saved);
    }

    /**
     * Searches departments by name.
     * @param name the search term
     * @return list of matching departments
     */
    public List<DepartmentDTO> searchByName(String name) {
        log.debug("Searching departments by name: {}", name);
        return departmentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets the count of employees in a department.
     * @param id the department ID
     * @return the employee count
     */
    public long getEmployeeCount(Long id) {
        log.debug("Getting employee count for department: {}", id);
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        return departmentRepository.countEmployeesByDepartmentId(id);
    }
}
