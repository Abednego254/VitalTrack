package app.action;

import app.ejb.HospitalTechnicianEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.Cohort12Framework;
import app.model.HospitalTechnician;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RequestScoped
@Action(value = "technician", label = "Technician Management")
public class TechnicianAction {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @Inject
    private Cohort12Framework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalTechnician.class, technicianEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalTechnician.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(HttpServletRequest request) throws Exception {
        // SECURITY: Only ADMIN can add new technicians
        String userRole = (String) request.getSession().getAttribute("role");
        if (!"ADMIN".equals(userRole)) {
            return new ActionResponse("<div class='error-message'>Only Hospital Admins can register new Technicians.</div>");
        }

        HospitalTechnician technician = new HospitalTechnician();
        technician.setName(request.getParameter("name"));
        technician.setSpecialization(request.getParameter("specialization"));
        technician.setContactInfo(request.getParameter("contactInfo"));
        technician.setEmail(request.getParameter("email"));
        technician.setStatus(request.getParameter("status"));

        technicianEJB.save(technician);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        technicianEJB.delete(id);
        return list();
    }
}
