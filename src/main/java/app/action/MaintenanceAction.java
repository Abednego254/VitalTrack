package app.action;

import app.ejb.MaintenanceLogEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.VitalTrackFramework;
import app.model.MaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Action(value = "maintenancelog", label = "Maintenance History", role = "ADMIN,TECHNICIAN")
public class MaintenanceAction {

    @EJB
    private MaintenanceLogEJB maintenanceEJB;

    @Inject
    private VitalTrackFramework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(MaintenanceLog.class, maintenanceEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(MaintenanceLog.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(HttpServletRequest request) throws Exception {
        MaintenanceLog log = new MaintenanceLog();

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

    @ActionGetMethod("edit/{id}")
    public ActionResponse edit(@ActionPathParam("id") Long id) throws Exception {
        MaintenanceLog log = maintenanceEJB.findById(id);
        return new ActionResponse(framework.htmlEditForm(MaintenanceLog.class, log));
    }

    @ActionPostMethod("update")
    public ActionResponse update(HttpServletRequest request) throws Exception {
        MaintenanceLog log = new MaintenanceLog();

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            log.setId(Long.parseLong(idParam));
        }

        String eqId = request.getParameter("equipmentId");
        log.setEquipmentId(eqId != null && !eqId.isEmpty() ? Long.parseLong(eqId) : null);
        log.setActionTaken(request.getParameter("actionTaken"));
        log.setNotes(request.getParameter("notes"));

        String dateStr = request.getParameter("serviceDate");
        if (dateStr != null && !dateStr.isEmpty()) {
            log.setServiceDate(java.sql.Date.valueOf(dateStr));
        }

        // Preserve the technician from session
        Long techId = (Long) request.getSession().getAttribute("techId");
        log.setTechnicianId(techId);

        maintenanceEJB.save(log);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        maintenanceEJB.delete(id);
        return list();
    }
}
