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

   // -------------------------------------------------------------------------
   // Sub-department queries
   // -------------------------------------------------------------------------

   /**
    * Returns all root (top-level) departments — those without a parent.
    */
   public List<Department> findRootDepartments() {
      return list("parentDepartment is null");
   }

   /**
    * Returns all active root departments.
    */
   public List<Department> findActiveRootDepartments() {
      return list("parentDepartment is null and active = true");
   }

   /**
    * Returns the direct sub-departments of the given parent department.
    */
   public List<Department> findSubDepartmentsByParentId(Long parentId) {
      return list("parentDepartment.id", parentId);
   }

   /**
    * Returns the active direct sub-departments of the given parent department.
    */
   public List<Department> findActiveSubDepartmentsByParentId(Long parentId) {
      return list("parentDepartment.id = ?1 and active = true", parentId);
   }

   /**
    * Fetches a department together with its parent and sub-departments in a
    * single query to avoid N+1 issues.
    */
   public Optional<Department> findByIdWithHierarchy(Long id) {
      return getEntityManager()
         .createQuery(
            "SELECT d FROM Department d " +
               "LEFT JOIN FETCH d.manager " +
               "LEFT JOIN FETCH d.parentDepartment " +
               "LEFT JOIN FETCH d.subDepartments " +
               "WHERE d.id = :id",
            Department.class)
         .setParameter("id", id)
         .getResultStream()
         .findFirst();
   }

   /**
    * Checks whether a department has any sub-departments.
    */
   public boolean hasSubDepartments(Long departmentId) {
      return count("parentDepartment.id", departmentId) > 0;
   }

   /**
    * Counts all sub-departments (direct children) of the given parent.
    */
   public long countSubDepartmentsByParentId(Long parentId) {
      return count("parentDepartment.id", parentId);
   }
}
