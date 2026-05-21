package app.action;

import app.ejb.HospitalNurseEJB;
import app.ejb.HospitalTechnicianEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/set-password")
public class SetPasswordAction extends HttpServlet {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @EJB
    private HospitalNurseEJB nurseEJB;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (session == null || role == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        if (password != null && password.equals(confirm)) {
            try {
                if ("NURSE".equals(role)) {
                    Long nurseId = (Long) session.getAttribute("nurseId");
                    nurseEJB.setPassword(nurseId, password);
                } else {
                    Long techId = (Long) session.getAttribute("techId");
                    technicianEJB.setPassword(techId, password);
                }
                // Update session so they don't get redirected to set-password again
                session.removeAttribute("role"); // force re-auth after password set
                resp.sendRedirect(req.getContextPath() + "/login");
            } catch (Exception e) {
                throw new ServletException("Failed to set password: " + e.getMessage(), e);
            }
        } else {
            req.setAttribute("error", "Passwords do not match!");
            req.getRequestDispatcher("/set-password.jsp").forward(req, resp);
        }
    }
}
