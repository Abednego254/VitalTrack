package app.model;

import app.framework.Cohort12Form;
import app.framework.Cohort12FormField;
import app.framework.Cohort12Table;
import app.framework.Cohort12TableCol;
import app.framework.DbColumn;
import app.framework.DbTable;
import app.framework.PageMenuItem;

import java.io.Serializable;
import java.util.Date;

@DbTable(name = "HospitalMedicalSupply")
@Cohort12Table(label = "Medical Supplies", addLink = "medicalsupply/add", deleteLink = "medicalsupply/delete")
@Cohort12Form(label = "Medical Supply", actionUrl = "medicalsupply/save")
public class HospitalMedicalSupply implements Serializable {

    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;
    @DbColumn(name = "name", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Name")
    @Cohort12FormField(label = "Supply Name", placeholder = "e.g. Surgical Gloves")
    private String name;
    @DbColumn(name = "category", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Category")
    @Cohort12FormField(label = "Category", placeholder = "e.g. Consumables")
    private String category;
    @DbColumn(name = "quantity", type = "int")
    @Cohort12TableCol(label = "Stock")
    @Cohort12FormField(label = "Initial Quantity", placeholder = "100")
    private int quantity;
    @DbColumn(name = "unitOfMeasure", type = "VARCHAR(255)")
    @Cohort12TableCol(label = "Unit")
    @Cohort12FormField(label = "Unit of Measure", placeholder = "Boxes")
    private String unitOfMeasure;
    @DbColumn(name = "expiryDate", type = "DATE")
    @Cohort12TableCol(label = "Expiry")
    @Cohort12FormField(label = "Expiry Date", placeholder = "YYYY-MM-DD")
    private Date expiryDate;
    @DbColumn(name = "reorderLevel", type = "int")
    @Cohort12FormField(label = "Reorder Level", placeholder = "10")
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
