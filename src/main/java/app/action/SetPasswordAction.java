package app.action;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("techId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        Long techId = (Long) session.getAttribute("techId");

        if (password != null && password.equals(confirm)) {
            try {
                technicianEJB.setPassword(techId, password);
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            } catch (Exception e) {
                throw new ServletException("Failed to set password: " + e.getMessage(), e);
            }
        } else {
            req.setAttribute("error", "Passwords do not match!");
            req.getRequestDispatcher("/set-password.jsp").forward(req, resp);
        }
    }
}
