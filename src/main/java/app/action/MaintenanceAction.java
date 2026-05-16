package app.action;

import app.ejb.HospitalMaintenanceLogEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.VitalTrackFramework;
import app.model.HospitalMaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RequestScoped
@Action(value = "maintenancelog", label = "Maintenance History")
public class MaintenanceAction {

    @EJB
    private HospitalMaintenanceLogEJB maintenanceEJB;

    @Inject
    private VitalTrackFramework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalMaintenanceLog.class, maintenanceEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalMaintenanceLog.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(HttpServletRequest request) throws Exception {
        HospitalMaintenanceLog log = new HospitalMaintenanceLog();

        String eqId = request.getParameter("equipmentId");
        Long techId = (Long) request.getSession().getAttribute("techId");
        
        log.setEquipmentId(eqId != null && !eqId.isEmpty() ? Long.parseLong(eqId) : null);
        log.setTechnicianId(techId);
        log.setActionTaken(request.getParameter("actionTaken"));
        log.setNotes(request.getParameter("notes"));

        String dateStr = request.getParameter("serviceDate");
        if (dateStr != null && !dateStr.isEmpty()) {
            log.setServiceDate(java.sql.Date.valueOf(dateStr));
        }

        maintenanceEJB.save(log);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        maintenanceEJB.delete(id);
        return list();
    }
}
