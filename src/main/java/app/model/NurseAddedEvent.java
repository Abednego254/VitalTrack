package app.model;

public class NurseAddedEvent {
    private HospitalNurse nurse;
    private String dummyPassword;

    public NurseAddedEvent(HospitalNurse nurse, String dummyPassword) {
        this.nurse = nurse;
        this.dummyPassword = dummyPassword;
    }

    public HospitalNurse getNurse() {
        return nurse;
    }

    public String getDummyPassword() {
        return dummyPassword;
    }
}
