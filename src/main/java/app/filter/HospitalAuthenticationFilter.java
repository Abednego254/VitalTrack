package app.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * [CONCEPT: Web Filter / Interceptor]
 * This is the Bouncer at the Front Door.
 * It stands in front of the ENTIRE application ("/*").
 */
@WebFilter(urlPatterns = {"/*"})
public class HospitalAuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getServletPath();

        // 1. Let people pass if they are just trying to login or logout
        //    Also, allow CSS, JS, and images to load freely
        boolean isLoginRequest = path.equals("/login") || path.equals("/login.jsp");
        boolean isLogoutRequest = path.equals("/logout");
        boolean isStaticResource = path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png");

        if (isLoginRequest || isLogoutRequest || isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. For all other rooms, check if they have a VIP pass (Session)
        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        
        if (isLoggedIn) {
            // They have a pass! Let them in.
            filterChain.doFilter(request, response);
        } else {
            // No pass! Kick them back to the login page.
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {}
}