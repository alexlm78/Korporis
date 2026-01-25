package dev.kreraker.korporis.model;

/**
 * Enumeration representing different types of employment contracts.
 */
public enum ContractType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    TEMPORARY("Temporary"),
    CONTRACTOR("Contractor"),
    INTERN("Intern"),
    FREELANCE("Freelance");

    private final String displayName;

    ContractType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
