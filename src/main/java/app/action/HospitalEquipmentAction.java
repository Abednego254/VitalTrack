package app.action;

import app.model.HospitalEquipment;
import app.utility.MaintenanceService;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/equipment")
public class HospitalEquipmentAction extends HospitalBaseAction<HospitalEquipment> {
    @jakarta.inject.Inject
    @app.utility.MaintenanceQualifier(app.utility.MaintenanceChoice.URGENT)
    private MaintenanceService urgentHelper;

//    // We can also ask for the Standard one if we want:
//    @jakarta.inject.Inject
//    @app.utility.MaintenanceQualifier(app.utility.MaintenanceChoice.STANDARD)
//    private app.utility.MaintenanceService standardHelper;

}
