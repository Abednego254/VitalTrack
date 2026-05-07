package app.action;

import app.ejb.HospitalEquipmentEJB;
import app.model.HospitalEquipment;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.sql.Date.valueOf;

@WebServlet("/equipment")
public class HospitalEquipmentAction extends HospitalBaseAction<HospitalEquipment> {

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<HospitalEquipment> items = equipmentEJB.findAll();

            request.setAttribute("items", items);

            String view = request.getParameter("view");
            if ("list".equals(view)) {
                request.getRequestDispatcher("/equipment-list.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/equipment.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Equipment EJB had an accident: " + e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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

            String nextCal = request.getParameter("nextCalibrationDate");
            if (nextCal != null && !nextCal.isEmpty()) {
                equipment.setNextCalibrationDate(valueOf(nextCal));
            }

            equipmentEJB.save(equipment);

            response.sendRedirect(request.getContextPath() + "/equipment?view=list");
        } catch (Exception e) {
            throw new ServletException("Equipment EJB had an accident: " + e.getMessage(), e);
        }
    }
}
