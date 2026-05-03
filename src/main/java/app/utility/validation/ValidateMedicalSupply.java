package app.utility.validation;

import app.model.HospitalEquipment;
import app.model.HospitalMedicalSupply;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;


@Named("ValidMedicalSupply")
@ApplicationScoped
public class ValidateMedicalSupply implements Validate<HospitalMedicalSupply> {

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidMedicalSupply' is checking the supply...");
    }

    @Override
    public boolean process(HospitalMedicalSupply medicalSupply) {
        if (medicalSupply == null) return false;
        if (medicalSupply.getName() == null || medicalSupply.getName().trim().isEmpty()) return false;
        if (medicalSupply.getCategory() == null || medicalSupply.getCategory().trim().isEmpty()) return false;
        if (medicalSupply.getQuantity() <= 0) return false;
        if (medicalSupply.getUnitOfMeasure() == null || medicalSupply.getUnitOfMeasure().trim().isEmpty()) return false;
        if (medicalSupply.getReorderLevel() <= 0) return false;

        // If all checks pass, we let it through!
        return true;
    }
}
