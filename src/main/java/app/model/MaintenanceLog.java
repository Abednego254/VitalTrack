package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.Date;

@Entity
@Table(name = "hospital_maintenance_log")
@VitalTrackTable(label = "Maintenance History", addLink = "maintenancelog/add", editLink = "maintenancelog/edit", deleteLink = "maintenancelog/delete")
@VitalTrackForm(label = "Maintenance Log", actionUrl = "maintenancelog/save")
@XmlRootElement(name = "maintenancelog")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaintenanceLog extends BaseEntity {

    @Column(name = "equipment_id")
    @VitalTrackFormField(label = "Equipment", select = "equipmentId")
    @NotNull(message = "Equipment is required")
    private Long equipmentId;

    @Column(name = "technician_id")
    private Long technicianId;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", insertable = false, updatable = false)
    private Equipment equipment;

    @XmlTransient
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
    @NotNull(message = "Service date is required")
    @Past(message = "Service date must be in the past")
    private Date serviceDate;

    @Column(name = "action_taken")
    @VitalTrackTableCol(label = "Action")
    @VitalTrackFormField(label = "Action Taken", placeholder = "Repair/Calibration")
    @NotBlank(message = "Action taken is required")
    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    @VitalTrackFormField(label = "Detailed Notes", placeholder = "... ")
    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    public MaintenanceLog(){}

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

    @JsonIgnore
    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @JsonIgnore
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
