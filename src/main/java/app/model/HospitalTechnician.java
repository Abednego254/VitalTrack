package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;

import java.io.Serializable;

@DbTable(name = "HospitalTechnician")
public class HospitalTechnician implements Serializable {
    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;
    @DbColumn(name = "name", type = "VARCHAR(255)")
    private String name;
    @DbColumn(name = "specialization", type = "VARCHAR(255)")
    private String specialization;
    @DbColumn(name = "contactInfo", type = "VARCHAR(255)")
    private String contactInfo;
    @DbColumn(name = "status", type = "VARCHAR(255)")
    private String status;
    @DbColumn(name = "email", type = "VARCHAR(255)")
    private String email;
    @DbColumn(name = "password", type = "VARCHAR(255)")
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
