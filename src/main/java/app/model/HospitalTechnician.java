package app.model;

import java.io.Serializable;

public class HospitalTechnician implements Serializable {
    private Long id;
    private String name;
    private String specialization;
    private String contactInfo;
    private TechnicianStatus technicianStatus;

    public HospitalTechnician() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public TechnicianStatus getTechnicianStatus() {
        return technicianStatus;
    }

    public void setTechnicianStatus(TechnicianStatus technicianStatus) {
        this.technicianStatus = technicianStatus;
    }
}
