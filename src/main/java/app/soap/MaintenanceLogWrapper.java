package app.soap;

import app.model.HospitalMaintenanceLog;
import jakarta.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "maintenancelogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaintenanceLogWrapper implements Serializable {

    private List<HospitalMaintenanceLog> log;

    public MaintenanceLogWrapper() {}

    public MaintenanceLogWrapper(List<HospitalMaintenanceLog> log) {
        this.log = log;
    }

    public List<HospitalMaintenanceLog> getLog() {
        return log;
    }

    public void setLog(List<HospitalMaintenanceLog> log) {
        this.log = log;
    }
}
