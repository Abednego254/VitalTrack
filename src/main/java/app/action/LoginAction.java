package app.action;

import app.ejb.UserEJB;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginAction extends HttpServlet {

    @EJB
    private UserEJB userEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Just show the login page
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = userEJB.authenticate(username, password);

            if (user != null) {
                // Success! Give them a VIP pass (Session)
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedInUser", user);
                session.setAttribute("username", user.getUsername());
                
                // Redirect to the main dashboard/index
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            } else {
                // Fail! Send them back to the login page with an error
                req.setAttribute("error", "Invalid username or password!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
