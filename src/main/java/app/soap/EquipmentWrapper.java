package app.soap;

import app.model.Equipment;
import jakarta.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "equipments")
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentWrapper implements Serializable {

    private List<Equipment> equipment;

    public EquipmentWrapper() {}

    public EquipmentWrapper(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}
