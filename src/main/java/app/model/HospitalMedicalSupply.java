package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hospital_medical_supply")
@VitalTrackTable(label = "Medical Supplies", addLink = "medicalsupply/add", deleteLink = "medicalsupply/delete")
@VitalTrackForm(label = "Medical Supply", actionUrl = "medicalsupply/save")
public class HospitalMedicalSupply extends BaseEntity {

    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Supply Name", placeholder = "e.g. Surgical Gloves")
    private String name;

    @Column
    @VitalTrackTableCol(label = "Category")
    @VitalTrackFormField(label = "Category", placeholder = "e.g. Consumables")
    private String category;

    @Column
    @VitalTrackTableCol(label = "Stock")
    @VitalTrackFormField(label = "Initial Quantity", placeholder = "100")
    private int quantity;

    @Column(name = "unit_of_measure")
    @VitalTrackTableCol(label = "Unit")
    @VitalTrackFormField(label = "Unit of Measure", placeholder = "Boxes")
    private String unitOfMeasure;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Expiry")
    @VitalTrackFormField(label = "Expiry Date", placeholder = "YYYY-MM-DD", type = "date")
    private Date expiryDate;

    @Column(name = "reorder_level")
    @VitalTrackFormField(label = "Reorder Level", placeholder = "10")
    private int reorderLevel;

    public HospitalMedicalSupply() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
}
