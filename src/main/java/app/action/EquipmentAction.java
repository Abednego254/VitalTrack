package app.action;

import app.ejb.EquipmentEJB;
import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionPathParam;
import app.framework.ActionPostMethod;
import app.framework.ActionResponse;
import app.framework.ActionRequestBody;
import app.framework.VitalTrackFramework;
import app.model.Equipment;
import app.model.User;
import app.utility.MaintenanceChoice;
import app.utility.MaintenanceQualifier;
import app.utility.MaintenanceService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestScoped
@Action(value = "equipment", label = "Equipment", role = "ADMIN,TECHNICIAN")
public class EquipmentAction {

    @EJB
    private EquipmentEJB equipmentEJB;

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
        return new ActionResponse(Equipment.class, equipmentEJB.findAll());
    }

    @ActionGetMethod("add")
    public ActionResponse add() throws Exception {
        return new ActionResponse(framework.htmlForm(Equipment.class));
    }

    @ActionPostMethod("save")
    public ActionResponse save(@ActionRequestBody Equipment equipment, HttpServletRequest request) throws Exception {

        // SMART LOGIC: Calculate next calibration date automatically
        String category = equipment.getMaintenanceCategory();
        if (equipment.getLastCalibrationDate() != null) {
            if ("URGENT".equals(category)) {
                equipment.setNextCalibrationDate(urgentMaintenance.calculateNextMaintenanceDate(equipment.getLastCalibrationDate()));
            } else {
                equipment.setNextCalibrationDate(standardMaintenance.calculateNextMaintenanceDate(equipment.getLastCalibrationDate()));
            }
        }

        // AUDIT TRAIL: Set the user who added this equipment
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loggedIn = session.getAttribute("loggedInUser");
            if (loggedIn instanceof User) {
                equipment.setCreateBy((User) loggedIn);
            }
        }

        equipmentEJB.save(equipment);
        return list();
    }

    @ActionGetMethod("edit/{id}")
    public ActionResponse edit(@ActionPathParam("id") Long id) throws Exception {
        Equipment equipment = equipmentEJB.findById(id);
        return new ActionResponse(framework.htmlEditForm(Equipment.class, equipment));
    }

    @ActionPostMethod("update")
    public ActionResponse update(@ActionRequestBody Equipment equipment, HttpServletRequest request) throws Exception {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            equipment.setId(Long.parseLong(idParam));
        }
        // Recalculate next calibration date on update
        String category = equipment.getMaintenanceCategory();
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
