package app.utility.validation;

import app.model.HospitalMaintenanceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("ValidMaintenanceLog")
@ApplicationScoped
public class ValidateMaintenanceLog implements Validate<HospitalMaintenanceLog> {

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidMaintenanceLog' is checking the maintenance log...");
    }

    @Override
    public boolean process(HospitalMaintenanceLog log) {
        if (log == null) return false;
        if (log.getEquipmentId() == null) return false;
        if (log.getTechnicianId() == null) return false;
        if (log.getServiceDate() == null) return false;
        if (log.getActionTaken() == null || log.getActionTaken().trim().isEmpty()) return false;
        
        return true;
    }
}
