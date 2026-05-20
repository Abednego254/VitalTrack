package app.model;

public enum EquipmentStatus {
    ACTIVE("Active"),
    FAULTY("Faulty"),
    UNDER_MAINTENANCE("Under Maintenance");

    private final String label;

    EquipmentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
