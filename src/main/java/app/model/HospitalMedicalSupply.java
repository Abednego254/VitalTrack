package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalMedicalSupply")
public class HospitalMedicalSupply implements Serializable {

    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;
    @DbColumn(name = "name", type = "VARCHAR(255)")
    private String name;
    @DbColumn(name = "category", type = "VARCHAR(255)")
    private String category;
    @DbColumn(name = "quantity", type = "int")
    private int quantity;
    @DbColumn(name = "unitOfMeasure", type = "VARCHAR(255)")
    private String unitOfMeasure;
    @DbColumn(name = "expiryDate", type = "DATE")
    private Date expiryDate;
    @DbColumn(name = "reorderLevel", type = "int")
    private int reorderLevel;

    public HospitalMedicalSupply() {
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
