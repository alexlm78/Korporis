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

    public DepartmentDTO findById(Long id) {
        LOG.debugf("Finding department by id: %d", id);
        Department department = departmentRepository.findByIdWithManager(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return DepartmentDTO.fromEntity(department);
    }

    public DepartmentDTO findByCode(String code) {
        LOG.debugf("Finding department by code: %s", code);
        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));
        return DepartmentDTO.fromEntity(department);
    }

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

        departmentRepository.persist(department);
        LOG.infof("Created department with id: %d and code: %s", department.id, department.code);
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

        LOG.infof("Updated department with id: %d", department.id);
        return DepartmentDTO.fromEntity(department);
    }

    @Transactional
    public void delete(Long id) {
        LOG.debugf("Deleting department with id: %d", id);

        Department department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        for (Employee employee : employees) {
            employee.department = null;
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
}
