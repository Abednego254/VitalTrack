package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("NURSE")
@VitalTrackTable(label = "Nurses", addLink = "nurse/add", deleteLink = "nurse/delete")
@VitalTrackForm(label = "Nurse", actionUrl = "nurse/save")
@XmlRootElement(name = "nurse")
@XmlAccessorType(XmlAccessType.FIELD)
public class HospitalNurse extends User {

    @Column
    @VitalTrackTableCol(label = "Specialization")
    @VitalTrackFormField(label = "Specialization", placeholder = "e.g. ICU, Pediatrics")
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
    @VitalTrackFormField(label = "Status", select = "nurseStatus")
    @NotNull(message = "Status is required")
    private NurseStatus status;

    public HospitalNurse() {}

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

    public NurseStatus getStatus() {
        return status;
    }

    public void setStatus(NurseStatus status) {
        this.status = status;
    }

    @Override
    @Transient
    public String getRole() {
        return "NURSE";
    }
}
