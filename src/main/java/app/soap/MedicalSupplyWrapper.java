package app.soap;

import app.model.HospitalMedicalSupply;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "medicalsupplies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicalSupplyWrapper implements Serializable {

    private List<HospitalMedicalSupply> supply;

    public MedicalSupplyWrapper() {}

    public MedicalSupplyWrapper(List<HospitalMedicalSupply> supply) {
        this.supply = supply;
    }

    public List<HospitalMedicalSupply> getSupply() {
        return supply;
    }

    public void setSupply(List<HospitalMedicalSupply> supply) {
        this.supply = supply;
    }
}
