package app.model;

public enum TechnicianStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String label;

    TechnicianStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
