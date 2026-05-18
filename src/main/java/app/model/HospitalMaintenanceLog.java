package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hospital_maintenance_log")
@VitalTrackTable(label = "Maintenance History", addLink = "maintenancelog/add", deleteLink = "maintenancelog/delete")
@VitalTrackForm(label = "Maintenance Log", actionUrl = "maintenancelog/save")
public class HospitalMaintenanceLog extends BaseEntity {

    @Column(name = "equipment_id")
    @VitalTrackFormField(label = "Equipment ID", placeholder = "1")
    private Long equipmentId;

    @Column(name = "technician_id")
    private Long technicianId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", insertable = false, updatable = false)
    private HospitalEquipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", insertable = false, updatable = false)
    private HospitalTechnician technician;

    @Transient
    @VitalTrackTableCol(label = "Equipment")
    private String equipmentName;

    @Transient
    @VitalTrackTableCol(label = "Technician")
    private String technicianName;

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

    public HospitalEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(HospitalEquipment equipment) {
        this.equipment = equipment;
    }

    public HospitalTechnician getTechnician() {
        return technician;
    }

    public void setTechnician(HospitalTechnician technician) {
        this.technician = technician;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
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
