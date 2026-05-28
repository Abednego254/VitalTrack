package app.soap;

import app.model.MaintenanceLog;
import jakarta.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "maintenancelogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaintenanceLogWrapper implements Serializable {

    private List<MaintenanceLog> log;

    public MaintenanceLogWrapper() {}

    public MaintenanceLogWrapper(List<MaintenanceLog> log) {
        this.log = log;
    }

    public List<MaintenanceLog> getLog() {
        return log;
    }

    public void setLog(List<MaintenanceLog> log) {
        this.log = log;
    }
}
