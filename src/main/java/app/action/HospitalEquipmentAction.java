package app.action;

import app.model.HospitalEquipment;
import app.utility.HospitalMaintenanceService;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/equipment")
public class HospitalEquipmentAction extends HospitalBaseAction<HospitalEquipment> {

    // [CONCEPT: @Inject]
    // The Waiter uses this magic word to ask the computer for the Helper!
    @jakarta.inject.Inject
    private HospitalMaintenanceService maintenanceService;

}
