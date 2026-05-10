package app.model;

import java.io.Serializable;

public class MedicalSupplyConsumedEvent implements Serializable {
    private final Long supplyId;
    private final int quantityConsumed;
    private final String supplyName;

    public MedicalSupplyConsumedEvent(Long supplyId, String supplyName, int quantityConsumed) {
        this.supplyId = supplyId;
        this.supplyName = supplyName;
        this.quantityConsumed = quantityConsumed;
    }

    public Long getSupplyId() {
        return supplyId;
    }

    public int getQuantityConsumed() {
        return quantityConsumed;
    }

    public String getSupplyName() {
        return supplyName;
    }
}
