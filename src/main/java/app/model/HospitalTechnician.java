package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "hospital_technician")
@Cohort12Table(label = "Technicians", addLink = "technician/add", deleteLink = "technician/delete")
@Cohort12Form(label = "Technician", actionUrl = "technician/save")
public class HospitalTechnician implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Cohort12TableCol(label = "Name")
    @Cohort12FormField(label = "Technician Name", placeholder = "e.g. John Doe")
    private String name;

    @Column
    @Cohort12TableCol(label = "Specialization")
    @Cohort12FormField(label = "Specialization", placeholder = "e.g. Biomedical")
    private String specialization;

    @Column(name = "contact_info")
    @Cohort12TableCol(label = "Contact")
    @Cohort12FormField(label = "Contact Info", placeholder = "+254...")
    private String contactInfo;

    @Column
    @Cohort12TableCol(label = "Status")
    @Cohort12FormField(label = "Status", placeholder = "Active")
    private String status;

    @Column(nullable = false, unique = true)
    @Cohort12TableCol(label = "Email")
    @Cohort12FormField(label = "Email Address", placeholder = "john@hospital.com")
    private String email;

    @Column(nullable = false)
    private String password;

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
