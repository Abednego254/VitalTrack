package app.utility.validation;

import app.model.HospitalEquipment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * [CONCEPT: CDI Specific Validator]
 * This is our specific Bouncer for Hospital Equipment!
 * 
 * @Named("ValidEquipment") tells CDI: "This is a Bouncer named ValidEquipment.
 * If anyone asks for a Validate<HospitalEquipment> named 'ValidEquipment', give them this one!"
 * 
 * @ApplicationScoped means there is only ONE bouncer of this type for the whole hospital.
 */
@Named("ValidEquipment")
@ApplicationScoped
public class ValidateEquipment implements Validate<HospitalEquipment> {

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidEquipment' is checking the equipment...");
    }

    @Override
    public boolean process(HospitalEquipment equipment) {
        // Here we check if the equipment has the required fields!
        // We can add any rules we want.
        if (equipment == null) return false;
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) return false;
        if (equipment.getSerialNumber() == null || equipment.getSerialNumber().trim().isEmpty()) return false;
        if (equipment.getStatus() == null || equipment.getStatus().trim().isEmpty()) return false;
        
        // If all checks pass, we let it through!
        return true;
    }
}
