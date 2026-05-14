package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import app.framework.DbColumn;
import app.framework.DbTable;
import app.framework.PageMenuItem;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalMaintenanceLog")
@Cohort12Table(label = "Maintenance History", addLink = "maintenancelog/add", deleteLink = "maintenancelog/delete")
@Cohort12Form(label = "Maintenance Log", actionUrl = "maintenancelog/save")
public class HospitalMaintenanceLog implements Serializable {
    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;
    @DbColumn(name = "equipmentId", type = "BIGINT")
    @Cohort12TableCol(label = "Equipment ID")
    @Cohort12FormField(label = "Equipment ID", placeholder = "1")
    private Long equipmentId;
    @DbColumn(name = "technicianId", type = "BIGINT")
    @Cohort12TableCol(label = "Tech ID")
    private Long technicianId;
    @DbColumn(name = "serviceDate", type = "DATE")
    @Cohort12TableCol(label = "Date")
    @Cohort12FormField(label = "Service Date", placeholder = "YYYY-MM-DD")
    private Date serviceDate;
    @DbColumn(name = "actionTaken", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Action")
    @Cohort12FormField(label = "Action Taken", placeholder = "Repair/Calibration")
    private String actionTaken;
    @DbColumn(name = "notes", type = "TEXT")
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
