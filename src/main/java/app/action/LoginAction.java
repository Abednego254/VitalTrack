package app.action;

import app.model.AuditTrail;
import jakarta.inject.Inject;
import jakarta.enterprise.event.Event;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginAction extends HttpServlet {

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            String username = req.getRemoteUser();
            if (username != null) {
                auditTrailEvent.fire(new AuditTrail("User '" + username + "' logged out."));
            }
            req.logout();
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        req.setAttribute("error", "Invalid email/username or password!");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
