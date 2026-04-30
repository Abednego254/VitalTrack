package app.model;

import java.io.Serializable;
import java.util.Date;

public class HospitalEquipment implements Serializable {
    private Long id;
    private String name;
    private String serialNumber;
    private Date purchaseDate;
    private Date lastCalibrationDate;
    private Date nextCalibrationDate;
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
