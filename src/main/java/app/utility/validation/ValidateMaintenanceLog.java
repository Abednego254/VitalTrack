package app.utility.validation;

import app.model.HospitalMaintenanceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidMaintenanceLog")
@ApplicationScoped
public class ValidateMaintenanceLog implements Validate<HospitalMaintenanceLog> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidMaintenanceLog' is checking the log constraints using JSR 380...");
    }

    @Override
    public boolean process(HospitalMaintenanceLog log) {
        if (log == null) return false;

        Set<ConstraintViolation<HospitalMaintenanceLog>> violations = validator.validate(log);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HospitalMaintenanceLog> violation : violations) {
                System.out.println(">>> Constraint violation in "
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
