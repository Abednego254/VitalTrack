package app.framework;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@ApplicationScoped
public class AppPage implements Serializable {

    @Inject
    private VitalTrackFramework framework;

    protected void display(HttpServletRequest request, HttpServletResponse response, String pageContent)
        throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>VitalTrack | Advanced Medical Logistics</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap' rel='stylesheet'>");
        out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css'>");
        out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");

        out.println("</head>");
        out.println("<body class='app-layout'>");

        /* DYNAMIC SIDEBAR */
        out.println(buildSidebar(request));

        out.println("<main class='main-content'>");
        out.println(pageContent);
        out.println("</main>");

        out.println("</body>");
        out.println("</html>");
    }

    private String buildSidebar(HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        String contextPath = request.getContextPath();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<aside class='sidebar'>");
        sb.append("<a href='").append(contextPath).append("/index.jsp' class='sidebar-logo'>VitalTrack</a>");
        sb.append("<nav class='sidebar-nav'>");

        // Scan for all @Action classes
        java.util.Set<Class<?>> actionClasses = app.utility.helper.ClassScanner.scanForAction("app.action");
        
        // Sort them if needed, but for now just iterate
        for (Class<?> clazz : actionClasses) {
            Action action = clazz.getAnnotation(Action.class);
            if (action != null && action.showLink()) {
                // Role Check
                if ("ADMIN".equals(action.role()) && !"ADMIN".equals(role)) {
                    continue; // Skip admin links for non-admins
                }

                String link = contextPath + "/vital/" + action.value() + "/" + action.pageLink();
                sb.append("<a href='").append(link).append("' class='sidebar-link'>")
                  .append(action.label().isEmpty() ? action.value() : action.label())
                  .append("</a>");
            }
        }

        sb.append("</nav>");
        sb.append("<div style='margin-top: auto; padding-top: 2rem; border-top: 1px solid var(--border);'>");
        sb.append("<a href='").append(contextPath).append("/logout' class='sidebar-link' style='color: var(--danger)'>Logout</a>");
        sb.append("</div>");
        sb.append("</aside>");
        
        return sb.toString();
    }
}
