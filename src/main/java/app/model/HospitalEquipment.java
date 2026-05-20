package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "hospital_equipment")
@VitalTrackTable(label = "Medical Equipment", addLink = "equipment/add", deleteLink = "equipment/delete")
@VitalTrackForm(label = "Medical Equipment", actionUrl = "equipment/save")
public class HospitalEquipment extends BaseEntity {
    
    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Equipment Name", placeholder = "e.g. Ventilator X-1")
    @NotBlank(message = "Equipment name must not be blank")
    @Size(min = 3, max = 50, message = "Equipment name must be between 3 and 50 characters")
    private String name;

    @Column(name = "serial_number", unique = true)
    @VitalTrackTableCol(label = "Serial Number")
    @VitalTrackFormField(label = "Serial Number", placeholder = "SN-12345")
    @NotBlank(message = "Serial number must not be blank")
    @Size(min = 5, max = 30, message = "Serial number must be between 5 and 30 characters")
    private String serialNumber;

    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Purchase Date")
    @VitalTrackFormField(label = "Purchase Date", placeholder = "YYYY-MM-DD", type = "date")
    @NotNull(message = "Purchase date must not be null")
    @Past(message = "Purchase date must be in the past")
    private Date purchaseDate;

    @Column(name = "last_calibration_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Last Cal")
    @VitalTrackFormField(label = "Last Calibration", placeholder = "YYYY-MM-DD", type = "date")
    @Past(message = "Last calibration date must be in the past")
    private Date lastCalibrationDate;

    @Column(name = "next_calibration_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Next Cal")
    private Date nextCalibrationDate;

    @Column
    @Convert(converter = EquipmentStatusConverter.class)
    @VitalTrackTableCol(label = "Status")
    @VitalTrackFormField(label = "Current Status", select = "equipmentStatus")
    @NotNull(message = "Status must not be null")
    private EquipmentStatus status;

    @Transient
    @VitalTrackFormField(label = "Maintenance Mode", select = "maintenanceCategory")
    private String maintenanceCategory;

    public HospitalEquipment() {
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

    public EquipmentStatus getStatus() {
        return status;
    }
 
    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public String getMaintenanceCategory() {
        return maintenanceCategory;
    }

    public void setMaintenanceCategory(String maintenanceCategory) {
        this.maintenanceCategory = maintenanceCategory;
    }
}
