package dev.kreraker.korporis.repository;

import dev.kreraker.korporis.model.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {

    public Optional<Department> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }

    public Optional<Department> findByCodeIgnoreCase(String code) {
        return find("lower(code)", code.toLowerCase()).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public boolean existsByCodeAndIdNot(String code, Long id) {
        return count("code = ?1 and id != ?2", code, id) > 0;
    }

    public List<Department> findByActiveTrue() {
        return list("active", true);
    }

    public List<Department> findByActiveFalse() {
        return list("active", false);
    }

    public List<Department> findByNameContainingIgnoreCase(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }

    public List<Department> findByLocationIgnoreCase(String location) {
        return list("lower(location)", location.toLowerCase());
    }

    public List<Department> findAllActiveWithEmployees() {
        return getEntityManager()
                .createQuery("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees WHERE d.active = true",
                        Department.class)
                .getResultList();
    }

    public Optional<Department> findByIdWithManager(Long id) {
        return getEntityManager()
                .createQuery("SELECT d FROM Department d LEFT JOIN FETCH d.manager WHERE d.id = :id",
                        Department.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public long countEmployeesByDepartmentId(Long departmentId) {
        return getEntityManager()
                .createQuery("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId", Long.class)
                .setParameter("departmentId", departmentId)
                .getSingleResult();
    }

    public List<Department> findByManagerId(Long managerId) {
        return list("manager.id", managerId);
    }
}
