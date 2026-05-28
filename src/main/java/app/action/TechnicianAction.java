package app.action;

import app.ejb.HospitalTechnicianEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.VitalTrackFramework;
import app.model.HospitalTechnician;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import app.framework.ActionRequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RequestScoped
@Action(value = "technician", label = "Technicians", role = "ADMIN")
public class TechnicianAction {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @Inject
    private VitalTrackFramework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalTechnician.class, technicianEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalTechnician.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(@ActionRequestBody HospitalTechnician technician, HttpServletRequest request) throws Exception {
        // Automatically set createdBy on new technician registration
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            Object loggedIn = session.getAttribute("loggedInUser");
            if (loggedIn instanceof app.model.User) {
                technician.setCreateBy((app.model.User) loggedIn);
            }
        }
        technicianEJB.save(technician);
        return list();
    }

    @ActionGetMethod("edit/{id}")
    public ActionResponse edit(@ActionPathParam("id") Long id) throws Exception {
        HospitalTechnician technician = technicianEJB.findById(id);
        return new ActionResponse(framework.htmlEditForm(HospitalTechnician.class, technician));
    }

    @ActionPostMethod("update")
    public ActionResponse update(@ActionRequestBody HospitalTechnician technician, HttpServletRequest request) throws Exception {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            technician.setId(Long.parseLong(idParam));
        }
        technicianEJB.save(technician);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        technicianEJB.delete(id);
        return list();
    }
}
