package app.utility.validation;

import app.model.HospitalNurse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidNurse")
@ApplicationScoped
public class ValidateNurse implements Validate<HospitalNurse> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidNurse' is checking the nurse constraints using JSR 380...");
    }

    @Override
    public boolean process(HospitalNurse nurse) {
        if (nurse == null) return false;

        Set<ConstraintViolation<HospitalNurse>> violations = validator.validate(nurse);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HospitalNurse> violation : violations) {
                System.out.println(">>> Constraint violation in "
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
