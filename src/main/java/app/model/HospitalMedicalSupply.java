package app.model;

import app.framework.VitalTrackForm;
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@Entity
@Table(name = "hospital_medical_supply")
@VitalTrackTable(label = "Medical Supplies", addLink = "medicalsupply/add", editLink = "medicalsupply/edit", deleteLink = "medicalsupply/delete")
@VitalTrackForm(label = "Medical Supply", actionUrl = "medicalsupply/save")
@XmlRootElement(name = "medicalsupply")
@XmlAccessorType(XmlAccessType.FIELD)
public class HospitalMedicalSupply extends BaseEntity {

    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Supply Name", placeholder = "e.g. Surgical Gloves")
    @NotBlank(message = "Supply name is required")
    @Size(min = 2, max = 100, message = "Supply name must be between 2 and 100 characters")
    private String name;

    @Column
    @VitalTrackTableCol(label = "Category")
    @VitalTrackFormField(label = "Category", placeholder = "e.g. Consumables")
    @NotBlank(message = "Category is required")
    private String category;

    @Column
    @VitalTrackTableCol(label = "Stock")
    @VitalTrackFormField(label = "Initial Quantity", placeholder = "100")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Column(name = "unit_of_measure")
    @VitalTrackTableCol(label = "Unit")
    @VitalTrackFormField(label = "Unit of Measure", placeholder = "Boxes")
    @NotBlank(message = "Unit of measure is required")
    private String unitOfMeasure;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    @VitalTrackTableCol(label = "Expiry")
    @VitalTrackFormField(label = "Expiry Date", placeholder = "YYYY-MM-DD", type = "date")
    @NotNull(message = "Expiry date is required")
    private Date expiryDate;

    @Column(name = "reorder_level")
    @VitalTrackFormField(label = "Reorder Level", placeholder = "10")
    @Min(value = 0, message = "Reorder level cannot be negative")
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
