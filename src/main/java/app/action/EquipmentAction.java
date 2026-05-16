package app.action;

import app.ejb.HospitalEquipmentEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.ActionRequestBody;
import app.framework.VitalTrackFramework;
import app.model.HospitalEquipment;
import app.utility.MaintenanceChoice;
import app.utility.MaintenanceQualifier;
import app.utility.MaintenanceService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Action(value = "equipment", label = "Equipment")
public class EquipmentAction {

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Inject
    private VitalTrackFramework framework;

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
    public ActionResponse save(@ActionRequestBody HospitalEquipment equipment, HttpServletRequest request) throws Exception {

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
