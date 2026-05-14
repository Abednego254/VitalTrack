package app.action;

import app.ejb.AuditTrailBean;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionResponse;
import app.framework.Cohort12Framework;
import app.model.AuditTrail;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
@Action(value = "audit-trail", label = "System Audit Trail")
public class SecurityAction {

    @Inject
    private AuditTrailBean auditTrailBean;

    @Inject
    private Cohort12Framework framework;

    @ActionGetMethod("list")
    public ActionResponse list(HttpServletRequest request) throws Exception {
        // SECURITY: Only ADMIN can view the audit trail
        String role = (String) request.getSession().getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return new ActionResponse("<div class='error-message'>Access Denied: Audit logs are restricted to Hospital Administrators.</div>");
        }

        List<AuditTrail> logs = auditTrailBean.findAll();
        // Sort by ID descending
        logs.sort((a, b) -> b.getId().compareTo(a.getId()));
        
        return new ActionResponse(AuditTrail.class, logs);
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id, HttpServletRequest request) throws Exception {
        // SECURITY: Only ADMIN can delete
        String role = (String) request.getSession().getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return new ActionResponse("<div class='error-message'>Access Denied: Only Hospital Administrators can delete audit logs.</div>");
        }
        auditTrailBean.delete(id);
        return list(request);
    }
}
