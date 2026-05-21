package app.model;

public enum NurseStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String label;

    NurseStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
