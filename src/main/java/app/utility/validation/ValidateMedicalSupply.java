package app.utility.validation;

import app.model.HospitalMedicalSupply;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidMedicalSupply")
@ApplicationScoped
public class ValidateMedicalSupply implements Validate<HospitalMedicalSupply> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidMedicalSupply' is checking the supply constraints using JSR 380...");
    }

    @Override
    public boolean process(HospitalMedicalSupply supply) {
        if (supply == null) return false;

        Set<ConstraintViolation<HospitalMedicalSupply>> violations = validator.validate(supply);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HospitalMedicalSupply> violation : violations) {
                System.out.println(">>> Constraint violation in "
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
