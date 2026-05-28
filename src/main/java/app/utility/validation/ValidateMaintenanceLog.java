package app.utility.validation;

import app.model.MaintenanceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Named("ValidMaintenanceLog")
@ApplicationScoped
public class ValidateMaintenanceLog implements Validate<MaintenanceLog> {

    @Inject
    private Validator validator;

    @Override
    public void printValidation() {
        System.out.println("Bouncer 'ValidMaintenanceLog' is checking the log constraints using JSR 380...");
    }

    @Override
    public boolean process(MaintenanceLog log) {
        if (log == null) return false;

        Set<ConstraintViolation<MaintenanceLog>> violations = validator.validate(log);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<MaintenanceLog> violation : violations) {
                System.out.println(">>> Constraint violation in "
                    + violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
