package app.security;

import app.ejb.UserEJB;
import app.model.User;
import app.model.HospitalTechnician;
import app.model.HospitalNurse;
import app.model.AuditTrail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@AutoApplySession
@ApplicationScoped
public class VitalTrackAuthMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private UserEJB userEJB;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request,
                                                HttpServletResponse response,
                                                HttpMessageContext httpMessageContext) {

        // 1. If request is already authenticated, accept it and do nothing.
        if (request.getUserPrincipal() != null) {
            return httpMessageContext.doNothing();
        }

        // 2. Intercept Login POST requests
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username != null && password != null) {
                CredentialValidationResult result = identityStoreHandler.validate(
                        new UsernamePasswordCredential(username, password)
                );

                if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                    String principalName = result.getCallerPrincipal().getName();
                    String role = result.getCallerGroups().iterator().next();

                    HttpSession session = request.getSession(true);
                    session.setAttribute("username", principalName);
                    session.setAttribute("role", role);

                    // Fetch the unified User object (can be User/Admin, HospitalTechnician, or HospitalNurse)
                    User user = userEJB.findByEmail(principalName);
                    session.setAttribute("loggedInUser", user);

                    if (user instanceof HospitalTechnician) {
                        session.setAttribute("techId", user.getId());
                    } else if (user instanceof HospitalNurse) {
                        session.setAttribute("nurseId", user.getId());
                    }

                    boolean mustSetPassword = false;
                    if (user != null && (user.getPassword() == null || user.getPassword().startsWith("VT-TEMP-"))) {
                        mustSetPassword = true;
                    }

                    auditTrailEvent.fire(new AuditTrail("User '" + principalName + "' (" + role + ") logged in successfully via Jakarta Security."));

                    httpMessageContext.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());

                    // Redirect accordingly
                    try {
                        if (mustSetPassword) {
                            response.sendRedirect(request.getContextPath() + "/set-password.jsp");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/index.jsp");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return AuthenticationStatus.SEND_CONTINUE;
                }
            }
        }

        return httpMessageContext.doNothing();
    }
}