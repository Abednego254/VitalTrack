package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("TECHNICIAN")
@VitalTrackTable(label = "Technicians", addLink = "technician/add", editLink = "technician/edit", deleteLink = "technician/delete")
@VitalTrackForm(label = "Technician", actionUrl = "technician/save")
@XmlRootElement(name = "technician")
@XmlAccessorType(XmlAccessType.FIELD)
public class HospitalTechnician extends User {

    @Column
    @VitalTrackTableCol(label = "Specialization")
    @VitalTrackFormField(label = "Specialization", placeholder = "e.g. Biomedical")
    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Column(name = "contact_info")
    @VitalTrackTableCol(label = "Contact")
    @VitalTrackFormField(label = "Contact Info", placeholder = "+254...")
    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    @Column
    @Enumerated(EnumType.STRING)
    @VitalTrackTableCol(label = "Status")
    @VitalTrackFormField(label = "Status", select = "technicianStatus")
    @NotNull(message = "Status is required")
    private TechnicianStatus status;

    @VitalTrackTableCol(label = "Completed Jobs")
    @Formula("(select count(*) from hospital_maintenance_log m where m.technician_id = id)")
    private Integer completedJobsCount;

    public HospitalTechnician() {}

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

    public TechnicianStatus getStatus() {
        return status;
    }

    public void setStatus(TechnicianStatus status) {
        this.status = status;
    }

    public Integer getCompletedJobsCount() {
        return completedJobsCount;
    }

    public void setCompletedJobsCount(Integer completedJobsCount) {
        this.completedJobsCount = completedJobsCount;
    }

    @Override
    @Transient
    public String getRole() {
        return "TECHNICIAN";
    }
}
