package app.action;

import app.ejb.HospitalNurseEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.VitalTrackFramework;
import app.model.HospitalNurse;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import app.framework.ActionRequestBody;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Action(value = "nurse", label = "Nurses", role = "ADMIN")
public class NurseAction {

    @EJB
    private HospitalNurseEJB nurseEJB;

    @Inject
    private VitalTrackFramework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalNurse.class, nurseEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalNurse.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(@ActionRequestBody HospitalNurse nurse, HttpServletRequest request) throws Exception {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            Object loggedIn = session.getAttribute("loggedInUser");
            if (loggedIn instanceof app.model.User) {
                nurse.setCreateBy((app.model.User) loggedIn);
            }
        }
        nurseEJB.save(nurse);
        return list();
    }

    @ActionGetMethod("edit/{id}")
    public ActionResponse edit(@ActionPathParam("id") Long id) throws Exception {
        HospitalNurse nurse = nurseEJB.findById(id);
        return new ActionResponse(framework.htmlEditForm(HospitalNurse.class, nurse));
    }

    @ActionPostMethod("update")
    public ActionResponse update(@ActionRequestBody HospitalNurse nurse, HttpServletRequest request) throws Exception {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            nurse.setId(Long.parseLong(idParam));
        }
        nurseEJB.save(nurse);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        nurseEJB.delete(id);
        return list();
    }
}
