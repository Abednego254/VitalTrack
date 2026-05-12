package app.action;

import app.ejb.AuditTrailBean;
import app.model.AuditTrail;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/audit-trail")
public class AuditTrailAction extends HospitalBaseAction<AuditTrail> {

    @Inject
    private AuditTrailBean auditTrailBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // SECURITY: Only ADMIN can view the audit trail
        String role = (String) request.getSession().getAttribute("role");
        if (!"ADMIN".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Audit logs are restricted to Hospital Administrators.");
            return;
        }

        try {
            List<AuditTrail> logs = auditTrailBean.findAll();
            
            // Sort by ID descending to show newest logs first
            logs.sort((a, b) -> b.getId().compareTo(a.getId()));
            
            request.setAttribute("logs", logs);
            request.getRequestDispatcher("/audit-trail.jsp").forward(request, response);
            
        } catch (Exception e) {
            throw new ServletException("Failed to fetch audit logs: " + e.getMessage(), e);
        }
    }
}
