package app.action;

import app.model.HospitalTechnician;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/technicians")
public class HospitalTechnicianAction extends HospitalBaseAction<HospitalTechnician> {
}
