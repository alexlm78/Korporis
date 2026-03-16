package dev.kreraker.korporis.repository;

import dev.kreraker.korporis.model.ContractType;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.model.EmployeeStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public Optional<Employee> findByEmployeeCode(String employeeCode) {
        return find("employeeCode", employeeCode).firstResultOptional();
    }

    public Optional<Employee> findByDpi(String dpi) {
        return find("dpi", dpi).firstResultOptional();
    }

    public Optional<Employee> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsByEmployeeCode(String employeeCode) {
        return count("employeeCode", employeeCode) > 0;
    }

    public boolean existsByDpi(String dpi) {
        return count("dpi", dpi) > 0;
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    public boolean existsByDpiAndIdNot(String dpi, Long id) {
        return count("dpi = ?1 and id != ?2", dpi, id) > 0;
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return count("email = ?1 and id != ?2", email, id) > 0;
    }

    public List<Employee> findByStatus(EmployeeStatus status) {
        return list("status", status);
    }

    public List<Employee> findAllActive() {
        return findByStatus(EmployeeStatus.ACTIVE);
    }

    public List<Employee> findByDepartmentId(Long departmentId) {
        return list("department.id", departmentId);
    }

    public List<Employee> findBySupervisorId(Long supervisorId) {
        return list("supervisor.id", supervisorId);
    }

    public List<Employee> findBySupervisorIsNull() {
        return list("supervisor is null");
    }

    public List<Employee> findByContractType(ContractType contractType) {
        return list("contractType", contractType);
    }

    public List<Employee> findByNameContaining(String name) {
        return getEntityManager()
                .createQuery("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) "
                        + "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))", Employee.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate) {
        return list("hireDate between ?1 and ?2", startDate, endDate);
    }

    public List<Employee> findByHireDateAfter(LocalDate date) {
        return list("hireDate > ?1", date);
    }

    public Optional<Employee> findByIdWithDepartment(Long id) {
        return getEntityManager()
                .createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public Optional<Employee> findByIdWithSupervisor(Long id) {
        return getEntityManager()
                .createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.supervisor WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public Optional<Employee> findByIdWithRelationships(Long id) {
        return getEntityManager()
                .createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.department LEFT JOIN FETCH e.supervisor "
                        + "WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public List<Employee> findAllWithDepartment() {
        return getEntityManager()
                .createQuery("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.department", Employee.class)
                .getResultList();
    }

    public long countByDepartmentId(Long departmentId) {
        return count("department.id", departmentId);
    }

    public long countByStatus(EmployeeStatus status) {
        return count("status", status);
    }

    public Integer findMaxEmployeeCodeNumber() {
        return getEntityManager()
                .createQuery("SELECT MAX(CAST(SUBSTRING(e.employeeCode, 5) AS int)) FROM Employee e "
                        + "WHERE e.employeeCode LIKE 'EMP-%'", Integer.class)
                .getSingleResult();
    }

    public List<Employee> findByPositionContainingIgnoreCase(String position) {
        return list("lower(position) like lower(?1)", "%" + position + "%");
    }

    public boolean existsById(Long id) {
        return count("id", id) > 0;
    }
}
