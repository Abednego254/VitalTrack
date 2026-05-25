package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "hospital_nurse")
@VitalTrackTable(label = "Nurses", addLink = "nurse/add", deleteLink = "nurse/delete")
@VitalTrackForm(label = "Nurse", actionUrl = "nurse/save")
@XmlRootElement(name = "nurse")
@XmlAccessorType(XmlAccessType.FIELD)
public class HospitalNurse extends BaseEntity {

    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Nurse Name", placeholder = "e.g. Jane Doe")
    @NotBlank(message = "Nurse name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

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

    @Column(nullable = false, unique = true)
    @VitalTrackTableCol(label = "Email")
    @VitalTrackFormField(label = "Email Address", placeholder = "jane@hospital.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    public HospitalNurse() {}

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

    public NurseStatus getStatus() {
        return status;
    }

    public void setStatus(NurseStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
