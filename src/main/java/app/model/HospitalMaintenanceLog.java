package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalMaintenanceLog")
public class HospitalMaintenanceLog implements Serializable {
    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;
    @DbColumn(name = "equipmentId", type = "Long")
    private Long equipmentId;
    @DbColumn(name = "technicianId", type = "Long")
    private Long technicianId;
    @DbColumn(name = "serviceDate", type = "DATE")
    private Date serviceDate;
    @DbColumn(name = "actionTaken", type = "VARCHAR(255)")
    private String actionTaken;
    @DbColumn(name = "notes", type = "VARCHAR(255)")
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
