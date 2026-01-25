package dev.kreraker.korporis.service;

import dev.kreraker.korporis.dto.CreateEmployeeRequest;
import dev.kreraker.korporis.dto.EmployeeDTO;
import dev.kreraker.korporis.dto.UpdateEmployeeRequest;
import dev.kreraker.korporis.exception.BusinessException;
import dev.kreraker.korporis.exception.DuplicateResourceException;
import dev.kreraker.korporis.exception.ResourceNotFoundException;
import dev.kreraker.korporis.model.Department;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.model.EmployeeStatus;
import dev.kreraker.korporis.repository.DepartmentRepository;
import dev.kreraker.korporis.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing employees.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    /**
     * Retrieves all employees.
     * @return list of all employees
     */
    public List<EmployeeDTO> findAll() {
        log.debug("Finding all employees");
        return employeeRepository.findAllWithDepartment().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all active employees.
     * @return list of active employees
     */
    public List<EmployeeDTO> findAllActive() {
        log.debug("Finding all active employees");
        return employeeRepository.findAllActive().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an employee by ID.
     * @param id the employee ID
     * @return the employee DTO
     * @throws ResourceNotFoundException if employee not found
     */
    public EmployeeDTO findById(Long id) {
        log.debug("Finding employee by id: {}", id);
        Employee employee = employeeRepository.findByIdWithRelationships(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Retrieves an employee by employee code.
     * @param employeeCode the employee code
     * @return the employee DTO
     * @throws ResourceNotFoundException if employee not found
     */
    public EmployeeDTO findByEmployeeCode(String employeeCode) {
        log.debug("Finding employee by code: {}", employeeCode);
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeCode", employeeCode));
        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Creates a new employee.
     * @param request the create request
     * @return the created employee DTO
     * @throws DuplicateResourceException if DPI or email already exists
     */
    @Transactional
    public EmployeeDTO create(CreateEmployeeRequest request) {
        log.debug("Creating employee with DPI: {}", request.getDpi());

        // Check for duplicate DPI
        if (employeeRepository.existsByDpi(request.getDpi())) {
            throw new DuplicateResourceException("Employee", "DPI", request.getDpi());
        }

        // Check for duplicate email
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", request.getEmail());
        }

        // Generate employee code
        String employeeCode = generateEmployeeCode();

        Employee employee = Employee.builder()
                .employeeCode(employeeCode)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dpi(request.getDpi())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .hireDate(request.getHireDate())
                .position(request.getPosition())
                .salary(request.getSalary())
                .contractType(request.getContractType())
                .status(EmployeeStatus.ACTIVE)
                .build();

        // Set department if provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            employee.setDepartment(department);
        }

        // Set supervisor if provided
        if (request.getSupervisorId() != null) {
            Employee supervisor = employeeRepository.findById(request.getSupervisorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getSupervisorId()));
            employee.setSupervisor(supervisor);
        }

        Employee saved = employeeRepository.save(employee);
        log.info("Created employee with id: {} and code: {}", saved.getId(), saved.getEmployeeCode());
        return EmployeeDTO.fromEntity(saved);
    }

    /**
     * Updates an existing employee.
     * @param id the employee ID
     * @param request the update request
     * @return the updated employee DTO
     * @throws ResourceNotFoundException if employee not found
     * @throws DuplicateResourceException if new DPI or email already exists
     */
    @Transactional
    public EmployeeDTO update(Long id, UpdateEmployeeRequest request) {
        log.debug("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Check for duplicate DPI if being changed
        if (request.getDpi() != null && !request.getDpi().equals(employee.getDpi())) {
            if (employeeRepository.existsByDpiAndIdNot(request.getDpi(), id)) {
                throw new DuplicateResourceException("Employee", "DPI", request.getDpi());
            }
            employee.setDpi(request.getDpi());
        }

        // Check for duplicate email if being changed
        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(employee.getEmail())) {
            if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException("Employee", "email", request.getEmail());
            }
            employee.setEmail(request.getEmail());
        }

        // Update personal information
        if (request.getFirstName() != null) {
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
        }
        if (request.getBirthDate() != null) {
            employee.setBirthDate(request.getBirthDate());
        }
        if (request.getGender() != null) {
            employee.setGender(request.getGender());
        }

        // Update contact information
        if (request.getPhone() != null) {
            employee.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        // Update employment information
        if (request.getHireDate() != null) {
            employee.setHireDate(request.getHireDate());
        }
        if (request.getTerminationDate() != null) {
            employee.setTerminationDate(request.getTerminationDate());
        }
        if (request.getPosition() != null) {
            employee.setPosition(request.getPosition());
        }
        if (request.getSalary() != null) {
            employee.setSalary(request.getSalary());
        }
        if (request.getContractType() != null) {
            employee.setContractType(request.getContractType());
        }
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }

        // Update department if provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            employee.setDepartment(department);
        }

        // Update supervisor if provided
        if (request.getSupervisorId() != null) {
            if (request.getSupervisorId().equals(id)) {
                throw new BusinessException("An employee cannot be their own supervisor");
            }
            Employee supervisor = employeeRepository.findById(request.getSupervisorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getSupervisorId()));
            
            // Check for circular reference
            if (isCircularReference(employee, supervisor)) {
                throw new BusinessException("Circular supervisor reference detected");
            }
            employee.setSupervisor(supervisor);
        }

        Employee saved = employeeRepository.save(employee);
        log.info("Updated employee with id: {}", saved.getId());
        return EmployeeDTO.fromEntity(saved);
    }

    /**
     * Deletes an employee by ID.
     * @param id the employee ID
     * @throws ResourceNotFoundException if employee not found
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Remove supervisor reference from subordinates
        List<Employee> subordinates = employeeRepository.findBySupervisorId(id);
        for (Employee subordinate : subordinates) {
            subordinate.setSupervisor(null);
        }
        employeeRepository.saveAll(subordinates);

        // Remove manager reference from departments
        List<Department> managedDepartments = departmentRepository.findByManagerId(id);
        for (Department department : managedDepartments) {
            department.setManager(null);
        }
        departmentRepository.saveAll(managedDepartments);

        employeeRepository.delete(employee);
        log.info("Deleted employee with id: {}", id);
    }

    /**
     * Terminates an employee.
     * @param id the employee ID
     * @return the terminated employee DTO
     * @throws ResourceNotFoundException if employee not found
     */
    @Transactional
    public EmployeeDTO terminate(Long id) {
        log.debug("Terminating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.setTerminationDate(java.time.LocalDate.now());

        Employee saved = employeeRepository.save(employee);
        log.info("Terminated employee with id: {}", id);
        return EmployeeDTO.fromEntity(saved);
    }

    /**
     * Retrieves employees by department.
     * @param departmentId the department ID
     * @return list of employees in the department
     */
    public List<EmployeeDTO> findByDepartment(Long departmentId) {
        log.debug("Finding employees by department: {}", departmentId);
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves subordinates of an employee.
     * @param supervisorId the supervisor's employee ID
     * @return list of subordinates
     */
    public List<EmployeeDTO> findSubordinates(Long supervisorId) {
        log.debug("Finding subordinates of employee: {}", supervisorId);
        if (!employeeRepository.existsById(supervisorId)) {
            throw new ResourceNotFoundException("Employee", "id", supervisorId);
        }
        return employeeRepository.findBySupervisorId(supervisorId).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Searches employees by name.
     * @param name the search term
     * @return list of matching employees
     */
    public List<EmployeeDTO> searchByName(String name) {
        log.debug("Searching employees by name: {}", name);
        return employeeRepository.findByNameContaining(name).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Generates a unique employee code in format EMP-XXXX.
     * @return the generated employee code
     */
    private String generateEmployeeCode() {
        Integer maxNumber = employeeRepository.findMaxEmployeeCodeNumber();
        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
        return String.format("EMP-%04d", nextNumber);
    }

    /**
     * Checks if setting a supervisor would create a circular reference.
     * @param employee the employee being updated
     * @param newSupervisor the proposed new supervisor
     * @return true if circular reference would be created
     */
    private boolean isCircularReference(Employee employee, Employee newSupervisor) {
        Employee current = newSupervisor;
        while (current != null) {
            if (current.getId().equals(employee.getId())) {
                return true;
            }
            current = current.getSupervisor();
        }
        return false;
    }
}
