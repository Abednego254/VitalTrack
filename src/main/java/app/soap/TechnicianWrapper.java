package app.soap;

import app.model.HospitalTechnician;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "technicians")
@XmlAccessorType(XmlAccessType.FIELD)
public class TechnicianWrapper implements Serializable {

    private List<HospitalTechnician> technician;

    public TechnicianWrapper() {}

    public TechnicianWrapper(List<HospitalTechnician> technician) {
        this.technician = technician;
    }

    public List<HospitalTechnician> getTechnician() {
        return technician;
    }

    public void setTechnician(List<HospitalTechnician> technician) {
        this.technician = technician;
    }
}
