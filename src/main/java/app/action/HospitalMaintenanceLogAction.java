package app.action;

import app.ejb.HospitalEquipmentEJB;
import app.ejb.HospitalMaintenanceLogEJB;
import app.model.HospitalEquipment;
import app.model.HospitalMaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/maintenancelog")
public class HospitalMaintenanceLogAction extends HospitalBaseAction<HospitalMaintenanceLog> {

    @EJB
    private HospitalMaintenanceLogEJB maintenanceEJB;

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<HospitalMaintenanceLog> items = maintenanceEJB.findAll();
            request.setAttribute("items", items);

            // Fetch equipment list for the dropdown
            List<HospitalEquipment> equipmentList = equipmentEJB.findAll();
            request.setAttribute("equipments", equipmentList);

            String view = request.getParameter("view");
            if ("list".equals(view)) {
                request.getRequestDispatcher("/maintenancelog-list.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/maintenancelog.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Maintenance EJB had an accident: " + e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HospitalMaintenanceLog log = new HospitalMaintenanceLog();

            String eqId = request.getParameter("equipmentId");
            // SMART LOGIC: Get technician ID from the session automatically
            Long techId = (Long) request.getSession().getAttribute("techId");
            
            log.setEquipmentId(eqId != null && !eqId.isEmpty() ? Long.parseLong(eqId) : null);
            log.setTechnicianId(techId);
            log.setActionTaken(request.getParameter("actionTaken"));
            log.setNotes(request.getParameter("notes"));

            String dateStr = request.getParameter("serviceDate");
            if (dateStr != null && !dateStr.isEmpty()) {
                log.setServiceDate(java.sql.Date.valueOf(dateStr));
            }

            maintenanceEJB.save(log);
            response.sendRedirect(request.getContextPath() + "/maintenancelog?view=list");
        } catch (Exception e) {
            throw new ServletException("Maintenance EJB had an accident: " + e.getMessage(), e);
        }
    }
}
