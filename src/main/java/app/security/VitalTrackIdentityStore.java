package app.security;

import app.ejb.UserEJB;
import app.ejb.HospitalTechnicianEJB;
import app.ejb.HospitalNurseEJB;
import app.model.User;
import app.model.HospitalTechnician;
import app.model.HospitalNurse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.HashSet;

@ApplicationScoped
public class VitalTrackIdentityStore implements IdentityStore {

    @Inject
    private UserEJB userEJB;

    @Inject
    private HospitalTechnicianEJB technicianEJB;

    @Inject
    private HospitalNurseEJB nurseEJB;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        String username = credential.getCaller();
        String password = credential.getPasswordAsString();

        // 1. Authenticate Admin User
        User user = userEJB.authenticate(username, password);
        if (user != null) {
            return new CredentialValidationResult(user.getUsername(), new HashSet<>(Collections.singletonList(user.getRole())));
        }

        // 2. Authenticate Technician
        HospitalTechnician tech = technicianEJB.authenticate(username, password);
        if (tech != null) {
            return new CredentialValidationResult(tech.getName(), new HashSet<>(Collections.singletonList("TECHNICIAN")));
        }

        // 3. Authenticate Nurse
        HospitalNurse nurse = nurseEJB.authenticate(username, password);
        if (nurse != null) {
            return new CredentialValidationResult(nurse.getName(), new HashSet<>(Collections.singletonList("NURSE")));
        }

        return CredentialValidationResult.INVALID_RESULT;
    }
}
