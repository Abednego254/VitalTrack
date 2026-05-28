package app.filter;

import app.utility.JwtUtility;
import app.utility.JwtUtility.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {
    "/api/*",
    "/MedicalSupplySoapService",
    "/EquipmentSoapService",
    "/NurseSoapService",
    "/TechnicianSoapService",
    "/MaintenanceLogSoapService"
})
public class ApiAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        String uri = req.getRequestURI();

        // /auth/login is intentionally open — you need it to GET your token
        if (path.contains("/auth/login") || uri.contains("/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        // No header or not a Bearer token — reject immediately
        if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
            sendUnauthorized(resp, "Unauthorized: A JWT Bearer token is required. Obtain one via POST /api/auth/login.");
            return;
        }

        try {
            String token = authHeader.substring("bearer ".length()).trim();
            Claims claims = JwtUtility.validateToken(token);

            if (claims == null) {
                sendUnauthorized(resp, "Unauthorized: Invalid or expired JWT token.");
                return;
            }

            String userRole = claims.getRole();

            // Role-Based Access Control — check the role against the requested path
            if (!checkAuthorization(userRole, path, uri)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Forbidden: Your role '" + userRole + "' does not have access to this resource.");
                return;
            }

            // Token is valid and role is authorized — proceed
            chain.doFilter(request, response);

        } catch (Exception e) {
            System.err.println(">>> API Filter Error: " + e.getMessage());
            sendUnauthorized(resp, "Unauthorized: Token validation failed.");
        }
    }

    private boolean checkAuthorization(String role, String path, String uri) {
        // ADMIN can access everything
        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        // NURSE can access medical supplies only
        if ("NURSE".equalsIgnoreCase(role)) {
            return path.contains("/supply") || path.contains("MedicalSupplySoapService")
                || uri.contains("/supply") || uri.contains("MedicalSupplySoapService");
        }

        // TECHNICIAN can access equipment and maintenance logs only
        if ("TECHNICIAN".equalsIgnoreCase(role)) {
            return path.contains("/equipment") || path.contains("/maintenance")
                || path.contains("EquipmentSoapService") || path.contains("MaintenanceLogSoapService")
                || uri.contains("/equipment") || uri.contains("/maintenance")
                || uri.contains("EquipmentSoapService") || uri.contains("MaintenanceLogSoapService");
        }

        return false;
    }

    private void sendUnauthorized(HttpServletResponse resp, String message) throws IOException {
        resp.setHeader("WWW-Authenticate", "Bearer realm=\"VitalTrack API\"");
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
