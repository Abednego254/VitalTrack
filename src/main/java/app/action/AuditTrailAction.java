package app.action;

import app.ejb.AuditTrailBean;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionResponse;
import app.model.AuditTrail;
import jakarta.inject.Inject;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
@Action(value = "audit-trail", label = "Security Logs", role = "ADMIN")
public class AuditTrailAction {

    @Inject
    private AuditTrailBean auditTrailBean;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        List<AuditTrail> logs = auditTrailBean.findAll();
        logs.sort((a, b) -> b.getId().compareTo(a.getId()));
        return new ActionResponse(AuditTrail.class, logs);
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        auditTrailBean.delete(id);
        List<AuditTrail> logs = auditTrailBean.findAll();
        logs.sort((a, b) -> b.getId().compareTo(a.getId()));
        return new ActionResponse(AuditTrail.class, logs);
    }
}
