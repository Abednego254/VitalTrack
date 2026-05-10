package app.action;

import app.ejb.HospitalTechnicianEJB;
import app.model.HospitalTechnician;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/technician")
public class HospitalTechnicianAction extends HospitalBaseAction<HospitalTechnician> {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<HospitalTechnician> items = technicianEJB.findAll();
            request.setAttribute("items", items);

            String view = request.getParameter("view");
            if ("list".equals(view)) {
                request.getRequestDispatcher("/technician-list.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/technician.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Technician EJB had an accident: " + e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // SECURITY: Only ADMIN can add new technicians
        String userRole = (String) request.getSession().getAttribute("role");
        if (!"ADMIN".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Hospital Admins can register new Technicians.");
            return;
        }

        try {
            HospitalTechnician technician = new HospitalTechnician();
            technician.setName(request.getParameter("name"));
            technician.setSpecialization(request.getParameter("specialization"));
            technician.setContactInfo(request.getParameter("contactInfo"));
            technician.setEmail(request.getParameter("email"));
            technician.setStatus(request.getParameter("status"));

            technicianEJB.save(technician);
            response.sendRedirect(request.getContextPath() + "/technician?view=list");
        } catch (Exception e) {
            throw new ServletException("Technician EJB had an accident: " + e.getMessage(), e);
        }
    }
}
