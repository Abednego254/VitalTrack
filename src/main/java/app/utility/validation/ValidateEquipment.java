package app.utility.validation;

import app.model.HospitalEquipment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("ValidEquipment")
@ApplicationScoped
public class ValidateEquipment implements Validate<HospitalEquipment> {

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidEquipment' is checking the equipment...");
    }

    @Override
    public boolean process(HospitalEquipment equipment) {

        if (equipment == null) return false;
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) return false;
        if (equipment.getSerialNumber() == null || equipment.getSerialNumber().trim().isEmpty()) return false;
        if (equipment.getStatus() == null || equipment.getStatus().trim().isEmpty()) return false;
        
        // If all checks pass, we let it through!
        return true;
    }
}
