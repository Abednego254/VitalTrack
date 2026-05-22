package app.soap;

import app.model.HospitalNurse;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "nurses")
@XmlAccessorType(XmlAccessType.FIELD)
public class NurseWrapper implements Serializable {

    private List<HospitalNurse> nurse;

    public NurseWrapper() {}

    public NurseWrapper(List<HospitalNurse> nurse) {
        this.nurse = nurse;
    }

    public List<HospitalNurse> getNurse() {
        return nurse;
    }

    public void setNurse(List<HospitalNurse> nurse) {
        this.nurse = nurse;
    }
}
