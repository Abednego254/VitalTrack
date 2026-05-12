package app.model;

public class TechnicianAddedEvent {
    private HospitalTechnician technician;
    private String dummyPassword;

    public TechnicianAddedEvent(HospitalTechnician technician, String dummyPassword) {
        this.technician = technician;
        this.dummyPassword = dummyPassword;
    }

    public HospitalTechnician getTechnician() {
        return technician;
    }

    public String getDummyPassword() {
        return dummyPassword;
    }
}
