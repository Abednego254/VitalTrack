package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalEquipment")
public class HospitalEquipment implements Serializable {
    
    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;

    @DbColumn(name = "name", type = "VARCHAR(255)")
    private String name;

    @DbColumn(name = "serialNumber", type = "VARCHAR(255)")
    private String serialNumber;

    @DbColumn(name = "purchaseDate", type = "DATE")
    private Date purchaseDate;

    @DbColumn(name = "lastCalibrationDate", type = "DATE")
    private Date lastCalibrationDate;

    @DbColumn(name = "nextCalibrationDate", type = "DATE")
    private Date nextCalibrationDate;

    @DbColumn(name = "status", type = "VARCHAR(255)")
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
