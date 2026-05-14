package app.action;

import app.ejb.HospitalEquipmentEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.Cohort12Framework;
import app.model.HospitalEquipment;
import app.utility.MaintenanceChoice;
import app.utility.MaintenanceQualifier;
import app.utility.MaintenanceService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static java.sql.Date.valueOf;

@RequestScoped
@Action(value = "equipment", label = "Equipment Management")
public class EquipmentAction {

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Inject
    private Cohort12Framework framework;

    @Inject
    @MaintenanceQualifier(MaintenanceChoice.STANDARD)
    private MaintenanceService standardMaintenance;

    @Inject
    @MaintenanceQualifier(MaintenanceChoice.URGENT)
    private MaintenanceService urgentMaintenance;

    @ActionGetMethod("list")
    public ActionResponse list() throws Exception {
        return new ActionResponse(HospitalEquipment.class, equipmentEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(HospitalEquipment.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(HttpServletRequest request) throws Exception {
        HospitalEquipment equipment = new HospitalEquipment();
        equipment.setName(request.getParameter("name"));
        equipment.setSerialNumber(request.getParameter("serialNumber"));
        equipment.setStatus(request.getParameter("status"));

        String purchaseDate = request.getParameter("purchaseDate");
        if (purchaseDate != null && !purchaseDate.isEmpty()) {
            equipment.setPurchaseDate(valueOf(purchaseDate));
        }

        String lastCal = request.getParameter("lastCalibrationDate");
        if (lastCal != null && !lastCal.isEmpty()) {
            equipment.setLastCalibrationDate(valueOf(lastCal));
        }

        // SMART LOGIC: Calculate next calibration date automatically
        String category = request.getParameter("maintenanceCategory");
        if (equipment.getLastCalibrationDate() != null) {
            if ("URGENT".equals(category)) {
                equipment.setNextCalibrationDate(urgentMaintenance.calculateNextMaintenanceDate(equipment.getLastCalibrationDate()));
            } else {
                equipment.setNextCalibrationDate(standardMaintenance.calculateNextMaintenanceDate(equipment.getLastCalibrationDate()));
            }
        }

        equipmentEJB.save(equipment);
        return list();
    }

    @ActionGetMethod("delete/{id}")
    public ActionResponse delete(@ActionPathParam("id") Long id) throws Exception {
        equipmentEJB.delete(id);
        return list();
    }
}
