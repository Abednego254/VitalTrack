package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hospital_equipment")
@VitalTrackTable(label = "Medical Equipment", addLink = "equipment/add", deleteLink = "equipment/delete")
@VitalTrackForm(label = "Medical Equipment", actionUrl = "equipment/save")
public class HospitalEquipment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Equipment Name", placeholder = "e.g. Ventilator X-1")
    private String name;

    @Column(name = "serial_number", unique = true)
    @VitalTrackTableCol(label = "Serial Number")
    @VitalTrackFormField(label = "Serial Number", placeholder = "SN-12345")
    private String serialNumber;

    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Purchase Date")
    @VitalTrackFormField(label = "Purchase Date", placeholder = "YYYY-MM-DD", type = "date")
    private Date purchaseDate;

    @Column(name = "last_calibration_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Last Cal")
    @VitalTrackFormField(label = "Last Calibration", placeholder = "YYYY-MM-DD", type = "date")
    private Date lastCalibrationDate;

    @Column(name = "next_calibration_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Next Cal")
    private Date nextCalibrationDate;

    @Column
    @VitalTrackTableCol(label = "Status")
    @VitalTrackFormField(label = "Current Status", placeholder = "Operational")
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
