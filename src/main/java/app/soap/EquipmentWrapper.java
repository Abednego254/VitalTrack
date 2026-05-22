package app.soap;

import app.model.HospitalEquipment;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "equipments")
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentWrapper implements Serializable {

    private List<HospitalEquipment> equipment;

    public EquipmentWrapper() {}

    public EquipmentWrapper(List<HospitalEquipment> equipment) {
        this.equipment = equipment;
    }

    public List<HospitalEquipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<HospitalEquipment> equipment) {
        this.equipment = equipment;
    }
}
