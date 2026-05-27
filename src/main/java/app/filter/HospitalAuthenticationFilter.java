package app.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class HospitalAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getServletPath();

        boolean isLoginRequest = path.equals("/login") || path.equals("/login.jsp");
        boolean isLogoutRequest = path.equals("/logout");
        boolean isStaticResource = path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png");
        boolean isApiRequest = path.startsWith("/api") || req.getRequestURI().contains("/api/");
        boolean isSoapApiRequest = path.contains("SoapApi") || path.contains("SoapService")
                || req.getRequestURI().contains("SoapApi") || req.getRequestURI().contains("SoapService");

        boolean isWebSocketRequest = path.startsWith("/ws") || req.getRequestURI().contains("/ws/")
                || path.equals("/audit_feeds") || path.equals("/stock_alerts") || path.equals("/chat")
                || req.getRequestURI().contains("/audit_feeds") || req.getRequestURI().contains("/stock_alerts") || req.getRequestURI().contains("/chat");

        if (isLoginRequest || isLogoutRequest || isStaticResource || isApiRequest || isSoapApiRequest || isWebSocketRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isLoggedIn = req.getUserPrincipal() != null || (session != null && session.getAttribute("loggedInUser") != null);
        
        if (isLoggedIn) {
            filterChain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

}