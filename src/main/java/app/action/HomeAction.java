package app.action;

import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestScoped
@Action(value = "home", label = "Dashboard", pageLink = "index")
public class HomeAction {

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

        // Equipment Card
        html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/equipment/list'\">")
            .append("<div class='card-icon'>EQ</div>")
            .append("<h3>Medical Equipment</h3>")
            .append("<p>Track calibration and maintenance cycles.</p>")
            .append("<span class='badge badge-success'>Online</span></div>");

        // Admin Only Cards
        if ("ADMIN".equals(role)) {
            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/medicalsupply/list'\">")
                .append("<div class='card-icon'>SP</div>")
                .append("<h3>Medical Supplies</h3>")
                .append("<p>Monitor inventory levels and expiration.</p>")
                .append("<span class='badge badge-success'>Stock OK</span></div>");

            html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/technician/list'\">")
                .append("<div class='card-icon'>TC</div>")
                .append("<h3>Technicians</h3>")
                .append("<p>Manage engineering personnel.</p>")
                .append("<span class='badge badge-warning'>Active</span></div>");
                
             html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/audit-trail/list'\">")
                .append("<div class='card-icon'>AU</div>")
                .append("<h3>Audit Logs</h3>")
                .append("<p>Advanced security tracking.</p>")
                .append("<span class='badge badge-info'>Secure</span></div>");
        }

        // Maintenance Card (Shared)
        html.append("<div class='card' onclick=\"location.href='").append(context).append("/vital/maintenancelog/list'\">")
            .append("<div class='card-icon'>ML</div>")
            .append("<h3>Maintenance Records</h3>")
            .append("<p>View history of all repairs.</p>")
            .append("<span class='badge badge-success'>Updated</span></div>");

        html.append("</section>");

        return new ActionResponse(html.toString());
    }
}
