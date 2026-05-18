package app.utility.validation;

import app.model.HospitalEquipment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidEquipment")
@ApplicationScoped
public class ValidateEquipment implements Validate<HospitalEquipment> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidEquipment' is checking the equipment constraints using JSR 380...");
    }

    @Override
    public boolean process(HospitalEquipment equipment) {
        if (equipment == null) return false;

        Set<ConstraintViolation<HospitalEquipment>> violations = validator.validate(equipment);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HospitalEquipment> violation : violations) {
                System.out.println(">>> Constraint violation in " 
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
