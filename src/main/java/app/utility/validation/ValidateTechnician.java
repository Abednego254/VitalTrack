package app.utility.validation;

import app.model.HospitalTechnician;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("ValidTechnician")
@ApplicationScoped
public class ValidateTechnician implements Validate<HospitalTechnician> {

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidTechnician' is checking the technician...");
    }

    @Override
    public boolean process(HospitalTechnician technician) {
        if (technician == null) return false;
        if (technician.getName() == null || technician.getName().trim().isEmpty()) return false;
        if (technician.getSpecialization() == null || technician.getSpecialization().trim().isEmpty()) return false;
        if (technician.getStatus() == null || technician.getStatus().trim().isEmpty()) return false;
        
        return true;
    }
}
