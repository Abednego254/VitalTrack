package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import app.framework.DbColumn;
import app.framework.DbTable;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalEquipment")
@Cohort12Table(label = "Medical Equipment", addLink = "equipment/add", deleteLink = "equipment/delete")
@Cohort12Form(label = "Medical Equipment", actionUrl = "equipment/save")
public class HospitalEquipment implements Serializable {
    
    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;

    @DbColumn(name = "name", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Name")
    @Cohort12FormField(label = "Equipment Name", placeholder = "e.g. Ventilator X-1")
    private String name;

    @DbColumn(name = "serialNumber", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Serial Number")
    @Cohort12FormField(label = "Serial Number", placeholder = "SN-12345")
    private String serialNumber;

    @DbColumn(name = "purchaseDate", type = "DATE")
    @Cohort12TableCol(label = "Purchase Date")
    @Cohort12FormField(label = "Purchase Date", placeholder = "YYYY-MM-DD")
    private Date purchaseDate;

    @DbColumn(name = "lastCalibrationDate", type = "DATE")
    @Cohort12TableCol(label = "Last Cal")
    @Cohort12FormField(label = "Last Calibration", placeholder = "YYYY-MM-DD")
    private Date lastCalibrationDate;

    @DbColumn(name = "nextCalibrationDate", type = "DATE")
    @Cohort12TableCol(label = "Next Cal")
    private Date nextCalibrationDate;

    @DbColumn(name = "status", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Status")
    @Cohort12FormField(label = "Current Status", placeholder = "Operational")
    private String status;

    public HospitalEquipment() {
    }

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getLastCalibrationDate() {
        return lastCalibrationDate;
    }

    public void setLastCalibrationDate(Date lastCalibrationDate) {
        this.lastCalibrationDate = lastCalibrationDate;
    }

    public Date getNextCalibrationDate() {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(Date nextCalibrationDate) {
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }

}
