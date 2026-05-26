package app.security;

import app.ejb.UserEJB;
import app.ejb.HospitalTechnicianEJB;
import app.ejb.HospitalNurseEJB;
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
    private HospitalTechnicianEJB technicianEJB;

    @Inject
    private HospitalNurseEJB nurseEJB;

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

                    // Establish session attributes for backward compatibility with JSP/Actions
                    HttpSession session = request.getSession(true);
                    session.setAttribute("username", principalName);
                    session.setAttribute("role", role);

                    Object entity = null;
                    boolean mustSetPassword = false;

                    if ("ADMIN".equals(role)) {
                        entity = userEJB.authenticate(username, password);
                    } else if ("TECHNICIAN".equals(role)) {
                        HospitalTechnician tech = technicianEJB.authenticate(username, password);
                        entity = tech;
                        session.setAttribute("techId", tech.getId());
                        if (tech.getPassword() == null || tech.getPassword().startsWith("VT-TEMP-")) {
                            mustSetPassword = true;
                        }
                    } else if ("NURSE".equals(role)) {
                        HospitalNurse nurse = nurseEJB.authenticate(username, password);
                        entity = nurse;
                        session.setAttribute("nurseId", nurse.getId());
                        if (nurse.getPassword() == null || nurse.getPassword().startsWith("VT-TEMP-")) {
                            mustSetPassword = true;
                        }
                    }
                    session.setAttribute("loggedInUser", entity);

                    auditTrailEvent.fire(new AuditTrail("User '" + principalName + "' (" + role + ") logged in successfully via Jakarta Security."));

                    // Notify container about successful login
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
