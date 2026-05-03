package app.action;

import app.ejb.HospitalMaintenanceLogEJB;
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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<HospitalMaintenanceLog> items = maintenanceEJB.findAll();
            request.setAttribute("items", items);

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
            String techId = request.getParameter("technicianId");
            log.setEquipmentId(eqId != null && !eqId.isEmpty() ? Long.parseLong(eqId) : null);
            log.setTechnicianId(techId != null && !techId.isEmpty() ? Long.parseLong(techId) : null);
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
