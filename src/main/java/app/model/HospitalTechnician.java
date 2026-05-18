package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "hospital_technician")
@VitalTrackTable(label = "Technicians", addLink = "technician/add", deleteLink = "technician/delete")
@VitalTrackForm(label = "Technician", actionUrl = "technician/save")
public class HospitalTechnician extends BaseEntity {
    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Technician Name", placeholder = "e.g. John Doe")
    @NotBlank(message = "Technician name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Column
    @VitalTrackTableCol(label = "Specialization")
    @VitalTrackFormField(label = "Specialization", placeholder = "e.g. Biomedical")
    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Column(name = "contact_info")
    @VitalTrackTableCol(label = "Contact")
    @VitalTrackFormField(label = "Contact Info", placeholder = "+254...")
    private String contactInfo;

    @Column
    @VitalTrackTableCol(label = "Status")
    @VitalTrackFormField(label = "Status", placeholder = "Active")
    private String status;

    @Column(nullable = false, unique = true)
    @VitalTrackTableCol(label = "Email")
    @VitalTrackFormField(label = "Email Address", placeholder = "john@hospital.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(nullable = false)
    private String password;

    public HospitalTechnician() {}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
