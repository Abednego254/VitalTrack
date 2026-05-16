package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hospital_maintenance_log")
@VitalTrackTable(label = "Maintenance History", addLink = "maintenancelog/add", deleteLink = "maintenancelog/delete")
@VitalTrackForm(label = "Maintenance Log", actionUrl = "maintenancelog/save")
public class HospitalMaintenanceLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_id")
    @VitalTrackTableCol(label = "Equipment ID")
    @VitalTrackFormField(label = "Equipment ID", placeholder = "1")
    private Long equipmentId;

    @Column(name = "technician_id")
    @VitalTrackTableCol(label = "Tech ID")
    private Long technicianId;

    @Column(name = "service_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Date")
    @VitalTrackFormField(label = "Service Date", placeholder = "YYYY-MM-DD", type = "date")
    private Date serviceDate;

    @Column(name = "action_taken")
    @VitalTrackTableCol(label = "Action")
    @VitalTrackFormField(label = "Action Taken", placeholder = "Repair/Calibration")
    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    @VitalTrackFormField(label = "Detailed Notes", placeholder = "... ")
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
