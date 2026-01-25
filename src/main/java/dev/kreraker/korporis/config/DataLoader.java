package dev.kreraker.korporis.config;

import dev.kreraker.korporis.model.*;
import dev.kreraker.korporis.repository.DepartmentRepository;
import dev.kreraker.korporis.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Loads sample data for development and testing.
 * Only runs with the 'h2' profile.
 */
@Component
@Profile("h2")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("Data already loaded, skipping...");
            return;
        }

        log.info("Loading sample data...");

        // Create Departments
        Department itDept = createDepartment("IT", "Information Technology", 
                "Manages all IT infrastructure and software development", "Building A, Floor 3");
        Department hrDept = createDepartment("HR", "Human Resources", 
                "Handles recruitment, employee relations, and benefits", "Building A, Floor 1");
        Department finDept = createDepartment("FIN", "Finance", 
                "Manages company finances, accounting, and budgeting", "Building B, Floor 2");
        Department salesDept = createDepartment("SALES", "Sales", 
                "Handles customer acquisition and sales operations", "Building C, Floor 1");
        Department opsDept = createDepartment("OPS", "Operations", 
                "Manages day-to-day business operations", "Building B, Floor 1");

        // Create Employees - IT Department
        Employee cto = createEmployee("EMP-0001", "Carlos", "Martinez", "1234567890123",
                LocalDate.of(1980, 5, 15), Gender.MALE, "carlos.martinez@korporis.com",
                "5555-1234", "Zone 10, Guatemala City", LocalDate.of(2015, 1, 10),
                "Chief Technology Officer", new BigDecimal("25000.00"), ContractType.FULL_TIME,
                itDept, null);

        Employee devLead = createEmployee("EMP-0002", "Maria", "Garcia", "2345678901234",
                LocalDate.of(1985, 8, 22), Gender.FEMALE, "maria.garcia@korporis.com",
                "5555-2345", "Zone 14, Guatemala City", LocalDate.of(2017, 3, 15),
                "Development Lead", new BigDecimal("18000.00"), ContractType.FULL_TIME,
                itDept, cto);

        Employee seniorDev = createEmployee("EMP-0003", "Juan", "Lopez", "3456789012345",
                LocalDate.of(1990, 2, 10), Gender.MALE, "juan.lopez@korporis.com",
                "5555-3456", "Zone 11, Guatemala City", LocalDate.of(2019, 6, 1),
                "Senior Developer", new BigDecimal("12000.00"), ContractType.FULL_TIME,
                itDept, devLead);

        Employee juniorDev = createEmployee("EMP-0004", "Ana", "Rodriguez", "4567890123456",
                LocalDate.of(1995, 11, 30), Gender.FEMALE, "ana.rodriguez@korporis.com",
                "5555-4567", "Zone 7, Guatemala City", LocalDate.of(2022, 1, 15),
                "Junior Developer", new BigDecimal("7000.00"), ContractType.FULL_TIME,
                itDept, devLead);

        // Create Employees - HR Department
        Employee hrDirector = createEmployee("EMP-0005", "Patricia", "Hernandez", "5678901234567",
                LocalDate.of(1978, 7, 8), Gender.FEMALE, "patricia.hernandez@korporis.com",
                "5555-5678", "Zone 15, Guatemala City", LocalDate.of(2014, 4, 20),
                "HR Director", new BigDecimal("20000.00"), ContractType.FULL_TIME,
                hrDept, null);

        Employee recruiter = createEmployee("EMP-0006", "Roberto", "Perez", "6789012345678",
                LocalDate.of(1992, 3, 25), Gender.MALE, "roberto.perez@korporis.com",
                "5555-6789", "Zone 12, Guatemala City", LocalDate.of(2020, 8, 10),
                "Recruiter", new BigDecimal("8000.00"), ContractType.FULL_TIME,
                hrDept, hrDirector);

        // Create Employees - Finance Department
        Employee cfo = createEmployee("EMP-0007", "Fernando", "Castillo", "7890123456789",
                LocalDate.of(1975, 12, 3), Gender.MALE, "fernando.castillo@korporis.com",
                "5555-7890", "Zone 10, Guatemala City", LocalDate.of(2013, 2, 1),
                "Chief Financial Officer", new BigDecimal("28000.00"), ContractType.FULL_TIME,
                finDept, null);

        Employee accountant = createEmployee("EMP-0008", "Lucia", "Morales", "8901234567890",
                LocalDate.of(1988, 9, 17), Gender.FEMALE, "lucia.morales@korporis.com",
                "5555-8901", "Zone 9, Guatemala City", LocalDate.of(2018, 5, 20),
                "Senior Accountant", new BigDecimal("10000.00"), ContractType.FULL_TIME,
                finDept, cfo);

        // Create Employees - Sales Department
        Employee salesDirector = createEmployee("EMP-0009", "Miguel", "Santos", "9012345678901",
                LocalDate.of(1982, 4, 12), Gender.MALE, "miguel.santos@korporis.com",
                "5555-9012", "Zone 13, Guatemala City", LocalDate.of(2016, 7, 5),
                "Sales Director", new BigDecimal("22000.00"), ContractType.FULL_TIME,
                salesDept, null);

        Employee salesRep = createEmployee("EMP-0010", "Carmen", "Flores", "0123456789012",
                LocalDate.of(1993, 6, 28), Gender.FEMALE, "carmen.flores@korporis.com",
                "5555-0123", "Zone 8, Guatemala City", LocalDate.of(2021, 3, 1),
                "Sales Representative", new BigDecimal("6500.00"), ContractType.FULL_TIME,
                salesDept, salesDirector);

        // Set department managers
        itDept.setManager(cto);
        hrDept.setManager(hrDirector);
        finDept.setManager(cfo);
        salesDept.setManager(salesDirector);

        departmentRepository.save(itDept);
        departmentRepository.save(hrDept);
        departmentRepository.save(finDept);
        departmentRepository.save(salesDept);

        log.info("Sample data loaded successfully!");
        log.info("Created {} departments", departmentRepository.count());
        log.info("Created {} employees", employeeRepository.count());
    }

    private Department createDepartment(String code, String name, String description, String location) {
        Department department = Department.builder()
                .code(code)
                .name(name)
                .description(description)
                .location(location)
                .active(true)
                .build();
        return departmentRepository.save(department);
    }

    private Employee createEmployee(String code, String firstName, String lastName, String dpi,
                                    LocalDate birthDate, Gender gender, String email, String phone,
                                    String address, LocalDate hireDate, String position,
                                    BigDecimal salary, ContractType contractType,
                                    Department department, Employee supervisor) {
        Employee employee = Employee.builder()
                .employeeCode(code)
                .firstName(firstName)
                .lastName(lastName)
                .dpi(dpi)
                .birthDate(birthDate)
                .gender(gender)
                .email(email)
                .phone(phone)
                .address(address)
                .hireDate(hireDate)
                .position(position)
                .salary(salary)
                .contractType(contractType)
                .status(EmployeeStatus.ACTIVE)
                .department(department)
                .supervisor(supervisor)
                .build();
        return employeeRepository.save(employee);
    }
}
