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

/**
 * [CONCEPT: EJB injection into a Servlet]
 *
 * We use @EJB (not @Inject) to inject EJBs into Servlets.
 * This tells WildFly: "Give me one of your pooled Stateless beans!"
 *
 * The Waiter (@WebServlet) now ONLY handles HTTP.
 * All database work is delegated to the EJB Specialist.
 */
@WebServlet("/equipment")
public class HospitalEquipmentAction extends HospitalBaseAction<HospitalEquipment> {

    // [CONCEPT: @EJB]
    // @EJB is the annotation used to inject an EJB into a Servlet.
    // WildFly picks a ready bean from its pool and gives it to us!
    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Ask the EJB Specialist to fetch all equipment
            List<HospitalEquipment> items = equipmentEJB.findAll();

            // 2. Put the list in the carry bag for the JSP
            request.setAttribute("items", items);

            // 3. Show the list page or the form page
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
            // 1. Build the sticky note from the form
            HospitalEquipment equipment = new HospitalEquipment();
            equipment.setName(request.getParameter("name"));
            equipment.setSerialNumber(request.getParameter("serialNumber"));
            equipment.setStatus(request.getParameter("status"));

            // 2. Hand it to the EJB Specialist to save
            equipmentEJB.save(equipment);

            // 3. Redirect to the list to see the result
            response.sendRedirect(request.getContextPath() + "/equipment?view=list");
        } catch (Exception e) {
            throw new ServletException("Equipment EJB had an accident: " + e.getMessage(), e);
        }
    }
}
