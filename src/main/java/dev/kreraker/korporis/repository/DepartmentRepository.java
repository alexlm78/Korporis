package dev.kreraker.korporis.repository;

import dev.kreraker.korporis.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Department entity operations.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Finds a department by its unique code.
     * @param code the department code
     * @return Optional containing the department if found
     */
    Optional<Department> findByCode(String code);

    /**
     * Finds a department by its code, ignoring case.
     * @param code the department code
     * @return Optional containing the department if found
     */
    Optional<Department> findByCodeIgnoreCase(String code);

    /**
     * Checks if a department with the given code exists.
     * @param code the department code
     * @return true if exists
     */
    boolean existsByCode(String code);

    /**
     * Checks if a department with the given code exists, excluding a specific ID.
     * @param code the department code
     * @param id the ID to exclude
     * @return true if exists
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * Finds all active departments.
     * @return list of active departments
     */
    List<Department> findByActiveTrue();

    /**
     * Finds all inactive departments.
     * @return list of inactive departments
     */
    List<Department> findByActiveFalse();

    /**
     * Finds departments by name containing the search term (case-insensitive).
     * @param name the search term
     * @return list of matching departments
     */
    List<Department> findByNameContainingIgnoreCase(String name);

    /**
     * Finds departments by location.
     * @param location the location
     * @return list of departments in the location
     */
    List<Department> findByLocationIgnoreCase(String location);

    /**
     * Finds all departments with their employees eagerly loaded.
     * @return list of departments with employees
     */
    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees WHERE d.active = true")
    List<Department> findAllActiveWithEmployees();

    /**
     * Finds a department by ID with its manager eagerly loaded.
     * @param id the department ID
     * @return Optional containing the department if found
     */
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.manager WHERE d.id = :id")
    Optional<Department> findByIdWithManager(@Param("id") Long id);

    /**
     * Counts employees in a department.
     * @param departmentId the department ID
     * @return number of employees
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
    long countEmployeesByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Finds departments managed by a specific employee.
     * @param managerId the manager's employee ID
     * @return list of departments managed by the employee
     */
    List<Department> findByManagerId(Long managerId);
}
