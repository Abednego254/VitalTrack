package app.utility.validation;

import app.model.HospitalTechnician;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidTechnician")
@ApplicationScoped
public class ValidateTechnician implements Validate<HospitalTechnician> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidTechnician' is checking the technician constraints using JSR 380...");
    }

    @Override
    public boolean process(HospitalTechnician technician) {
        if (technician == null) return false;

        Set<ConstraintViolation<HospitalTechnician>> violations = validator.validate(technician);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HospitalTechnician> violation : violations) {
                System.out.println(">>> Constraint violation in " 
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
