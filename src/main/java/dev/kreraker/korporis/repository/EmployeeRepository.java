package dev.kreraker.korporis.repository;

import dev.kreraker.korporis.model.ContractType;
import dev.kreraker.korporis.model.Employee;
import dev.kreraker.korporis.model.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their unique employee code.
     * @param employeeCode the employee code
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmployeeCode(String employeeCode);

    /**
     * Finds an employee by their DPI (national ID).
     * @param dpi the DPI number
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByDpi(String dpi);

    /**
     * Finds an employee by their email address.
     * @param email the email address
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Checks if an employee with the given employee code exists.
     * @param employeeCode the employee code
     * @return true if exists
     */
    boolean existsByEmployeeCode(String employeeCode);

    /**
     * Checks if an employee with the given DPI exists.
     * @param dpi the DPI number
     * @return true if exists
     */
    boolean existsByDpi(String dpi);

    /**
     * Checks if an employee with the given email exists.
     * @param email the email address
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Checks if an employee with the given DPI exists, excluding a specific ID.
     * @param dpi the DPI number
     * @param id the ID to exclude
     * @return true if exists
     */
    boolean existsByDpiAndIdNot(String dpi, Long id);

    /**
     * Checks if an employee with the given email exists, excluding a specific ID.
     * @param email the email address
     * @param id the ID to exclude
     * @return true if exists
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Finds all employees by their status.
     * @param status the employee status
     * @return list of employees with the given status
     */
    List<Employee> findByStatus(EmployeeStatus status);

    /**
     * Finds all active employees.
     * @return list of active employees
     */
    default List<Employee> findAllActive() {
        return findByStatus(EmployeeStatus.ACTIVE);
    }

    /**
     * Finds all employees in a specific department.
     * @param departmentId the department ID
     * @return list of employees in the department
     */
    List<Employee> findByDepartmentId(Long departmentId);

    /**
     * Finds all employees with a specific supervisor.
     * @param supervisorId the supervisor's employee ID
     * @return list of subordinates
     */
    List<Employee> findBySupervisorId(Long supervisorId);

    /**
     * Finds all employees without a supervisor (top-level employees).
     * @return list of employees without supervisors
     */
    List<Employee> findBySupervisorIsNull();

    /**
     * Finds employees by contract type.
     * @param contractType the contract type
     * @return list of employees with the given contract type
     */
    List<Employee> findByContractType(ContractType contractType);

    /**
     * Finds employees by name (first or last name containing the search term).
     * @param name the search term
     * @return list of matching employees
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByNameContaining(@Param("name") String name);

    /**
     * Finds employees hired between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return list of employees hired in the date range
     */
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Finds employees hired after a specific date.
     * @param date the date
     * @return list of employees hired after the date
     */
    List<Employee> findByHireDateAfter(LocalDate date);

    /**
     * Finds an employee by ID with department eagerly loaded.
     * @param id the employee ID
     * @return Optional containing the employee if found
     */
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE e.id = :id")
    Optional<Employee> findByIdWithDepartment(@Param("id") Long id);

    /**
     * Finds an employee by ID with supervisor eagerly loaded.
     * @param id the employee ID
     * @return Optional containing the employee if found
     */
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.supervisor WHERE e.id = :id")
    Optional<Employee> findByIdWithSupervisor(@Param("id") Long id);

    /**
     * Finds an employee by ID with all relationships eagerly loaded.
     * @param id the employee ID
     * @return Optional containing the employee if found
     */
    @Query("SELECT e FROM Employee e " +
           "LEFT JOIN FETCH e.department " +
           "LEFT JOIN FETCH e.supervisor " +
           "WHERE e.id = :id")
    Optional<Employee> findByIdWithRelationships(@Param("id") Long id);

    /**
     * Finds all employees with their departments eagerly loaded.
     * @return list of employees with departments
     */
    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.department")
    List<Employee> findAllWithDepartment();

    /**
     * Counts employees by department.
     * @param departmentId the department ID
     * @return number of employees in the department
     */
    long countByDepartmentId(Long departmentId);

    /**
     * Counts employees by status.
     * @param status the employee status
     * @return number of employees with the status
     */
    long countByStatus(EmployeeStatus status);

    /**
     * Finds the maximum employee code number for generating new codes.
     * @return the maximum code number or null if no employees exist
     */
    @Query("SELECT MAX(CAST(SUBSTRING(e.employeeCode, 5) AS int)) FROM Employee e WHERE e.employeeCode LIKE 'EMP-%'")
    Integer findMaxEmployeeCodeNumber();

    /**
     * Finds employees by position.
     * @param position the position title
     * @return list of employees with the given position
     */
    List<Employee> findByPositionContainingIgnoreCase(String position);
}
