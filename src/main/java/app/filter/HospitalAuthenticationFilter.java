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
        // WildFly publishes @WebService endpoints directly under the context root
        // using ServiceName/ClassName structure (e.g. /EquipmentSoapService/EquipmentSoapApi)
        boolean isSoapApiRequest = path.contains("SoapApi") || path.contains("SoapService")
                || req.getRequestURI().contains("SoapApi") || req.getRequestURI().contains("SoapService");

        if (isLoginRequest || isLogoutRequest || isStaticResource || isApiRequest || isSoapApiRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        
        if (isLoggedIn) {
            filterChain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

}