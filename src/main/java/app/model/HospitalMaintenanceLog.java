package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hospital_maintenance_log")
@Cohort12Table(label = "Maintenance History", addLink = "maintenancelog/add", deleteLink = "maintenancelog/delete")
@Cohort12Form(label = "Maintenance Log", actionUrl = "maintenancelog/save")
public class HospitalMaintenanceLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_id")
    @Cohort12TableCol(label = "Equipment ID")
    @Cohort12FormField(label = "Equipment ID", placeholder = "1")
    private Long equipmentId;

    @Column(name = "technician_id")
    @Cohort12TableCol(label = "Tech ID")
    private Long technicianId;

    @Column(name = "service_date")
    @Temporal(TemporalType.DATE)
    @Cohort12TableCol(label = "Date")
    @Cohort12FormField(label = "Service Date", placeholder = "YYYY-MM-DD", type = "date")
    private Date serviceDate;

    @Column(name = "action_taken")
    @Cohort12TableCol(label = "Action")
    @Cohort12FormField(label = "Action Taken", placeholder = "Repair/Calibration")
    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    @Cohort12FormField(label = "Detailed Notes", placeholder = "... ")
    private String notes;

    public HospitalMaintenanceLog(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
