package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hospital_equipment")
@Cohort12Table(label = "Medical Equipment", addLink = "equipment/add", deleteLink = "equipment/delete")
@Cohort12Form(label = "Medical Equipment", actionUrl = "equipment/save")
public class HospitalEquipment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Cohort12TableCol(label = "Name")
    @Cohort12FormField(label = "Equipment Name", placeholder = "e.g. Ventilator X-1")
    private String name;

    @Column(name = "serial_number", unique = true)
    @Cohort12TableCol(label = "Serial Number")
    @Cohort12FormField(label = "Serial Number", placeholder = "SN-12345")
    private String serialNumber;

    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    @Cohort12TableCol(label = "Purchase Date")
    @Cohort12FormField(label = "Purchase Date", placeholder = "YYYY-MM-DD", type = "date")
    private Date purchaseDate;

    @Column(name = "last_calibration_date")
    @Temporal(TemporalType.DATE)
    @Cohort12TableCol(label = "Last Cal")
    @Cohort12FormField(label = "Last Calibration", placeholder = "YYYY-MM-DD", type = "date")
    private Date lastCalibrationDate;

    @Column(name = "next_calibration_date")
    @Temporal(TemporalType.DATE)
    @Cohort12TableCol(label = "Next Cal")
    private Date nextCalibrationDate;

    @Column
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
