package dev.kreraker.korporis.model;

/**
 * Enumeration representing the employment status of an employee.
 */
public enum EmployeeStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave"),
    SUSPENDED("Suspended"),
    TERMINATED("Terminated"),
    RETIRED("Retired");

    private final String displayName;

    EmployeeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
