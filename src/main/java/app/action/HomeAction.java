package app.action;

import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionResponse;
import app.ejb.AuditTrailBean;
import app.model.AuditTrail;
import app.ejb.HospitalMedicalSupplyEJB;
import app.model.HospitalMedicalSupply;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

@RequestScoped
@Action(value = "home", label = "Dashboard", pageLink = "index")
public class HomeAction {

    @Inject
    private AuditTrailBean auditTrailBean;

    @Inject
    private HospitalMedicalSupplyEJB supplyEJB;

    @ActionGetMethod("index")
    public ActionResponse index(HttpServletRequest request) throws Exception {
        String role = (String) request.getSession().getAttribute("role");
        String context = request.getContextPath();
        
        StringBuilder html = new StringBuilder();
        html.append("<header class='hero'>");
        html.append("<h1>Welcome to VitalTrack</h1>");
        html.append("<p>Advanced Medical Logistics & Asset Management</p>");
        html.append("</header>");

        html.append("<section class='container'>");

        // Equipment Card - Visible to Admin and Technician
        if ("ADMIN".equals(role) || "TECHNICIAN".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/equipment/list'\">")
                .append("<div class='card-icon'>EQ</div>")
                .append("<h3>Medical Equipment</h3>")
                .append("<p>Track calibration and maintenance cycles.</p>")
                .append("<span class='badge badge-success'>Online</span></div>");
        }

        // Medical Supplies Card - Visible to Admin and Nurse
        if ("ADMIN".equals(role) || "NURSE".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/medicalsupply/list'\">")
                .append("<div class='card-icon'>SP</div>")
                .append("<h3>Medical Supplies</h3>")
                .append("<p>Monitor inventory levels and expiration.</p>")
                .append("<span class='badge badge-success'>Stock OK</span></div>");
        }

        // Technicians Card - Admin Only
        if ("ADMIN".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/technician/list'\">")
                .append("<div class='card-icon'>TC</div>")
                .append("<h3>Technicians</h3>")
                .append("<p>Manage engineering personnel.</p>")
                .append("<span class='badge badge-warning'>Active</span></div>");
        }

        // Nurses Card - Admin Only
        if ("ADMIN".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/nurse/list'\">")
                .append("<div class='card-icon'>NS</div>")
                .append("<h3>Nurses</h3>")
                .append("<p>Manage nursing staff.</p>")
                .append("<span class='badge badge-info'>Active</span></div>");
        }

        // Maintenance Card - Visible to Admin and Technician
        if ("ADMIN".equals(role) || "TECHNICIAN".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/maintenancelog/list'\">")
                .append("<div class='card-icon'>ML</div>")
                .append("<h3>Maintenance Records</h3>")
                .append("<p>View history of all repairs.</p>")
                .append("<span class='badge badge-success'>Updated</span></div>");
        }

        // Audit logs - Admin Only
        if ("ADMIN".equals(role)) {
             html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/audit-trail/list'\">")
                .append("<div class='card-icon'>AU</div>")
                .append("<h3>Audit Logs</h3>")
                .append("<p>Advanced security tracking.</p>")
                .append("<span class='badge badge-info'>Secure</span></div>");
        }

        html.append("</section>");

        // Stock Alert Feed / Live Warnings - Visible to Admin and Nurse
        if ("ADMIN".equals(role) || "NURSE".equals(role)) {
            List<HospitalMedicalSupply> lowStockSupplies = java.util.Collections.emptyList();
            try {
                lowStockSupplies = supplyEJB.findAll().stream()
                    .filter(s -> s.getQuantity() <= s.getReorderLevel())
                    .collect(Collectors.toList());
            } catch (Exception ignored) {}

            boolean hasLowStock = !lowStockSupplies.isEmpty();
            html.append("<section class='container' style='grid-template-columns: 1fr; margin-top: 1.5rem;'>");
            html.append("<div class='card glass' id='stock-warnings-card' style='border-left: 5px solid #ef4444; background: #fef2f2; color: #991b1b; max-width: 100%; width: 100%; ")
                .append(hasLowStock ? "" : "display: none;")
                .append("'>");
            html.append("<h3 style='color: #ef4444; margin: 0; display: flex; align-items: center; gap: 0.5rem;'><i class='fa-solid fa-circle-exclamation'></i> Critical Stock Warnings</h3>");
            html.append("<hr style='border:0; border-top:1px solid #fee2e2; margin: 1rem 0;' />");
            html.append("<ul id='stock-warnings-list' style='list-style: none; padding: 0; display: flex; flex-direction: column; gap: 0.6rem; font-weight: 500;'>");
            
            for (HospitalMedicalSupply s : lowStockSupplies) {
                html.append("<li style='display: flex; align-items: center; gap: 0.5rem;'>")
                    .append("<i class='fa-solid fa-triangle-exclamation' style='color: #dc2626;'></i> ")
                    .append("Warning: Medical Supply '").append(s.getName()).append("' is running low! Current stock: ")
                    .append(s.getQuantity()).append(" (Reorder level: ").append(s.getReorderLevel()).append(")")
                    .append("</li>");
            }
            
            html.append("</ul>");
            html.append("</div>");
            html.append("</section>");
        }

        // Activity Feed - Admin Only
        if ("ADMIN".equals(role)) {
            html.append("<section class='container' style='grid-template-columns: 1fr; margin-top: 2rem;'>");
            html.append("<div class='card glass' style='max-width: 100%; width: 100%;'>");
            html.append("<h3><i class='fa-solid fa-clock-rotate-left'></i> Live System Activity Feed</h3>");
            html.append("<hr style='border:0; border-top:1px solid var(--border); margin: 1rem 0;' />");
            html.append("<ul id='activity-feed-list' style='list-style: none; padding: 0; max-height: 250px; overflow-y: auto; display: flex; flex-direction: column; gap: 0.8rem;'>");

            try {
                List<AuditTrail> logs = auditTrailBean.findAll();
                List<AuditTrail> recent = logs.stream()
                        .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                        .limit(8)
                        .collect(Collectors.toList());
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                for (AuditTrail log : recent) {
                    boolean isUrgent = log.getAction().contains("CRITICAL") || log.getAction().contains("STOCK ALERT") || log.getAction().contains("WARNING");
                    html.append("<li class='activity-item' style='font-size: 0.95rem; border-bottom: 1px solid rgba(0,0,0,0.05); padding-bottom: 0.5rem; ")
                        .append(isUrgent ? "color: #ef4444; font-weight: bold;" : "")
                        .append("'>");
                    html.append("<span class='activity-time' style='color: var(--text-muted); font-weight: 600; margin-right: 0.8rem;'>[")
                        .append(sdf.format(log.getTimestamp())).append("]</span>");
                    html.append("<span class='activity-text'>").append(log.getAction()).append("</span>");
                    html.append("</li>");
                }
            } catch (Exception e) {
                html.append("<li>Failed to load recent system activities: ").append(e.getMessage()).append("</li>");
            }
            
            html.append("</ul>");
            html.append("</div>");
            html.append("</section>");
        }

        return new ActionResponse(html.toString());
    }
}
