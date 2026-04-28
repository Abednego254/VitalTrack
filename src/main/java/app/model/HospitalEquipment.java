package app.model;

import java.io.Serializable;
import java.util.Date;

public class HospitalEquipment implements Serializable {
    private Long id;
    private String name;
    private String serialNumber;
    private Date purchaseDate;
    private Date lastCallibrationDate;
    private Date nextCallibrationDate;
    private EquipmentStatus equipmentStatus;

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

    public Date getLastCallibrationDate() {
        return lastCallibrationDate;
    }

    public void setLastCalibrationDate(Date lastCallibrationDate) {
        this.lastCallibrationDate = lastCallibrationDate;
    }

    public Date getNextCallibrationDate() {
        return nextCallibrationDate;
    }

    public void setNextCalibrationDate(Date nextCallibrationDate) {
        this.nextCallibrationDate = nextCallibrationDate;
    }

    public EquipmentStatus getStatus() {
        return equipmentStatus;
    }

    public void setStatus(EquipmentStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

}
