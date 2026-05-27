package app.security;

import app.ejb.UserEJB;
import app.model.User;
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

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        String username = credential.getCaller();
        String password = credential.getPasswordAsString();

        // Single EJB call handles all user types (Admin, Technician, Nurse)!
        User user = userEJB.authenticate(username, password);
        if (user != null) {
            return new CredentialValidationResult(user.getEmail(), new HashSet<>(Collections.singletonList(user.getRole())));
        }

        return CredentialValidationResult.INVALID_RESULT;
    }
}
