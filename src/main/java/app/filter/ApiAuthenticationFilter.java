package app.filter;

import app.ejb.UserEJB;
import app.model.User;
import app.utility.JwtUtility;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter(urlPatterns = {
    "/api/*",
    "/MedicalSupplySoapService",
    "/EquipmentSoapService",
    "/NurseSoapService",
    "/TechnicianSoapService",
    "/MaintenanceLogSoapService"
})
public class ApiAuthenticationFilter implements Filter {

    @Inject
    private UserEJB userEJB;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        String uri = req.getRequestURI();

        // Bypass authentication for the login endpoint
        if (path.contains("/auth/login") || uri.contains("/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null) {
            sendUnauthorized(resp);
            return;
        }

        String userRole = null;

        try {
            if (authHeader.toLowerCase().startsWith("bearer ")) {
                // Validate JWT Bearer token
                String token = authHeader.substring("bearer ".length()).trim();
                JwtUtility.Claims claims = JwtUtility.validateToken(token);
                if (claims == null) {
                    sendUnauthorized(resp, "Unauthorized: Invalid or expired JWT token.");
                    return;
                }
                userRole = claims.getRole();
            } else if (authHeader.toLowerCase().startsWith("basic ")) {
                // Decode Base64 Basic credentials
                String base64Credentials = authHeader.substring("basic ".length()).trim();
                byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

                String[] values = credentials.split(":", 2);
                if (values.length != 2) {
                    sendUnauthorized(resp);
                    return;
                }

                String username = values[0];
                String password = values[1];

                // Validate against the database
                User user = userEJB.authenticate(username, password);
                if (user == null) {
                    sendUnauthorized(resp);
                    return;
                }
                userRole = user.getRole();
            } else {
                sendUnauthorized(resp);
                return;
            }

            // Perform Role-Based Access Control (RBAC)
            boolean isAuthorized = checkAuthorization(userRole, path, uri);
            if (!isAuthorized) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have the required role for this API.");
                return;
            }

            // Proceed if authenticated and authorized
            chain.doFilter(request, response);

        } catch (Exception e) {
            System.err.println("API Authentication Filter Error: " + e.getMessage());
            sendUnauthorized(resp);
        }
    }

    private boolean checkAuthorization(String role, String path, String uri) {
        // ADMIN can access everything
        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        // NURSE can access supplies
        if ("NURSE".equalsIgnoreCase(role)) {
            return path.contains("/supply") || path.contains("MedicalSupplySoapService") || uri.contains("/supply") || uri.contains("MedicalSupplySoapService");
        }

        // TECHNICIAN can access equipment and maintenance logs
        if ("TECHNICIAN".equalsIgnoreCase(role)) {
            return path.contains("/equipment") || path.contains("/maintenance") 
                || path.contains("EquipmentSoapService") || path.contains("MaintenanceLogSoapService")
                || uri.contains("/equipment") || uri.contains("/maintenance") 
                || uri.contains("EquipmentSoapService") || uri.contains("MaintenanceLogSoapService");
        }

        return false;
    }

    private void sendUnauthorized(HttpServletResponse resp) throws IOException {
        sendUnauthorized(resp, "Unauthorized: API access requires valid Basic Authentication or JWT Bearer Token.");
    }

    private void sendUnauthorized(HttpServletResponse resp, String message) throws IOException {
        resp.setHeader("WWW-Authenticate", "Basic realm=\"VitalTrack API\", Bearer realm=\"VitalTrack Token\"");
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
