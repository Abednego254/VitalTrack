package app.action;

import app.ejb.HospitalMedicalSupplyEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.VitalTrackFramework;
import app.model.HospitalMedicalSupply;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Action(value = "medicalsupply", label = "Medical Supplies", role = "ADMIN,NURSE")
public class SupplyAction {

    @EJB
    private HospitalMedicalSupplyEJB supplyEJB;

    @Inject
    private VitalTrackFramework framework;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalMedicalSupply.class, supplyEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalMedicalSupply.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(HttpServletRequest request) throws Exception {
        String mode = request.getParameter("mode");

        if ("consume".equals(mode)) {
            Long id = Long.parseLong(request.getParameter("id"));
            int qty = Integer.parseInt(request.getParameter("consumeQty"));
            String name = request.getParameter("name");
            supplyEJB.consume(id, name, qty);
            return list();
        }

        HospitalMedicalSupply supply = new HospitalMedicalSupply();
        supply.setName(request.getParameter("name"));
        supply.setCategory(request.getParameter("category"));
        supply.setUnitOfMeasure(request.getParameter("unitOfMeasure"));

        String qty = request.getParameter("quantity");
        supply.setQuantity(qty != null && !qty.isEmpty() ? Integer.parseInt(qty) : 0);

        String reorder = request.getParameter("reorderLevel");
        supply.setReorderLevel(reorder != null && !reorder.isEmpty() ? Integer.parseInt(reorder) : 10);

        String expiryDate = request.getParameter("expiryDate");
        if (expiryDate != null && !expiryDate.isEmpty()) {
            supply.setExpiryDate(java.sql.Date.valueOf(expiryDate));
        }

        supplyEJB.save(supply);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        supplyEJB.delete(id);
        return list();
    }
}
