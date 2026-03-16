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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeService {

    private static final Logger LOG = Logger.getLogger(EmployeeService.class);

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    DepartmentRepository departmentRepository;

    public List<EmployeeDTO> findAll() {
        LOG.debug("Finding all employees");
        return employeeRepository.findAllWithDepartment().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> findAllActive() {
        LOG.debug("Finding all active employees");
        return employeeRepository.findAllActive().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EmployeeDTO findById(Long id) {
        LOG.debugf("Finding employee by id: %d", id);
        Employee employee = employeeRepository.findByIdWithRelationships(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return EmployeeDTO.fromEntity(employee);
    }

    public EmployeeDTO findByEmployeeCode(String employeeCode) {
        LOG.debugf("Finding employee by code: %s", employeeCode);
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeCode", employeeCode));
        return EmployeeDTO.fromEntity(employee);
    }

    @Transactional
    public EmployeeDTO create(CreateEmployeeRequest request) {
        LOG.debugf("Creating employee with DPI: %s", request.dpi);

        if (employeeRepository.existsByDpi(request.dpi)) {
            throw new DuplicateResourceException("Employee", "DPI", request.dpi);
        }
        if (employeeRepository.existsByEmail(request.email)) {
            throw new DuplicateResourceException("Employee", "email", request.email);
        }

        String employeeCode = generateEmployeeCode();

        Employee employee = new Employee();
        employee.employeeCode = employeeCode;
        employee.firstName = request.firstName;
        employee.lastName = request.lastName;
        employee.dpi = request.dpi;
        employee.birthDate = request.birthDate;
        employee.gender = request.gender;
        employee.email = request.email;
        employee.phone = request.phone;
        employee.address = request.address;
        employee.hireDate = request.hireDate;
        employee.position = request.position;
        employee.salary = request.salary;
        employee.contractType = request.contractType;
        employee.status = EmployeeStatus.ACTIVE;

        if (request.departmentId != null) {
            Department department = departmentRepository.findByIdOptional(request.departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.departmentId));
            employee.department = department;
        }

        if (request.supervisorId != null) {
            Employee supervisor = employeeRepository.findByIdOptional(request.supervisorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.supervisorId));
            employee.supervisor = supervisor;
        }

        employeeRepository.persist(employee);
        LOG.infof("Created employee with id: %d and code: %s", employee.id, employee.employeeCode);
        return EmployeeDTO.fromEntity(employee);
    }

    @Transactional
    public EmployeeDTO update(Long id, UpdateEmployeeRequest request) {
        LOG.debugf("Updating employee with id: %d", id);

        Employee employee = employeeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        if (request.dpi != null && !request.dpi.equals(employee.dpi)) {
            if (employeeRepository.existsByDpiAndIdNot(request.dpi, id)) {
                throw new DuplicateResourceException("Employee", "DPI", request.dpi);
            }
            employee.dpi = request.dpi;
        }

        if (request.email != null && !request.email.equalsIgnoreCase(employee.email)) {
            if (employeeRepository.existsByEmailAndIdNot(request.email, id)) {
                throw new DuplicateResourceException("Employee", "email", request.email);
            }
            employee.email = request.email;
        }

        if (request.firstName != null) {
            employee.firstName = request.firstName;
        }
        if (request.lastName != null) {
            employee.lastName = request.lastName;
        }
        if (request.birthDate != null) {
            employee.birthDate = request.birthDate;
        }
        if (request.gender != null) {
            employee.gender = request.gender;
        }
        if (request.phone != null) {
            employee.phone = request.phone;
        }
        if (request.address != null) {
            employee.address = request.address;
        }
        if (request.hireDate != null) {
            employee.hireDate = request.hireDate;
        }
        if (request.terminationDate != null) {
            employee.terminationDate = request.terminationDate;
        }
        if (request.position != null) {
            employee.position = request.position;
        }
        if (request.salary != null) {
            employee.salary = request.salary;
        }
        if (request.contractType != null) {
            employee.contractType = request.contractType;
        }
        if (request.status != null) {
            employee.status = request.status;
        }

        if (request.departmentId != null) {
            Department department = departmentRepository.findByIdOptional(request.departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.departmentId));
            employee.department = department;
        }

        if (request.supervisorId != null) {
            if (request.supervisorId.equals(id)) {
                throw new BusinessException("An employee cannot be their own supervisor");
            }
            Employee supervisor = employeeRepository.findByIdOptional(request.supervisorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.supervisorId));

            if (isCircularReference(employee, supervisor)) {
                throw new BusinessException("Circular supervisor reference detected");
            }
            employee.supervisor = supervisor;
        }

        LOG.infof("Updated employee with id: %d", employee.id);
        return EmployeeDTO.fromEntity(employee);
    }

    @Transactional
    public void delete(Long id) {
        LOG.debugf("Deleting employee with id: %d", id);

        Employee employee = employeeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        List<Employee> subordinates = employeeRepository.findBySupervisorId(id);
        for (Employee subordinate : subordinates) {
            subordinate.supervisor = null;
        }

        List<Department> managedDepartments = departmentRepository.findByManagerId(id);
        for (Department department : managedDepartments) {
            department.manager = null;
        }

        employeeRepository.delete(employee);
        LOG.infof("Deleted employee with id: %d", id);
    }

    @Transactional
    public EmployeeDTO terminate(Long id) {
        LOG.debugf("Terminating employee with id: %d", id);

        Employee employee = employeeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.status = EmployeeStatus.TERMINATED;
        employee.terminationDate = LocalDate.now();

        LOG.infof("Terminated employee with id: %d", id);
        return EmployeeDTO.fromEntity(employee);
    }

    public List<EmployeeDTO> findByDepartment(Long departmentId) {
        LOG.debugf("Finding employees by department: %d", departmentId);
        if (departmentRepository.findByIdOptional(departmentId).isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> findSubordinates(Long supervisorId) {
        LOG.debugf("Finding subordinates of employee: %d", supervisorId);
        if (!employeeRepository.existsById(supervisorId)) {
            throw new ResourceNotFoundException("Employee", "id", supervisorId);
        }
        return employeeRepository.findBySupervisorId(supervisorId).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> searchByName(String name) {
        LOG.debugf("Searching employees by name: %s", name);
        return employeeRepository.findByNameContaining(name).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private String generateEmployeeCode() {
        Integer maxNumber = employeeRepository.findMaxEmployeeCodeNumber();
        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
        return String.format("EMP-%04d", nextNumber);
    }

    private boolean isCircularReference(Employee employee, Employee newSupervisor) {
        Employee current = newSupervisor;
        while (current != null) {
            if (current.id.equals(employee.id)) {
                return true;
            }
            current = current.supervisor;
        }
        return false;
    }
}
