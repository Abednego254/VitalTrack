package app.framework;

import app.utility.helper.ClassScanner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.Set;

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

        /* FLOATING CHAT WIDGET UI */
        out.println("<div id='chat-widget-wrapper' class='minimized'>");
        out.println("  <div id='chat-widget-toggle'>");
        out.println("    <span id='chat-status-dot'></span>");
        out.println("    <i class='fa-solid fa-comments'></i>");
        out.println("    <span class='chat-toggle-label'>Team Live Chat</span>");
        out.println("    <span id='chat-online-count' class='chat-online-badge'>0 Online</span>");
        out.println("  </div>");
        out.println("  <div id='chat-widget-window'>");
        out.println("    <div id='chat-widget-header'>");
        out.println("      <span class='chat-header-title'><i class='fa-solid fa-comments'></i> Operations Chat</span>");
        out.println("      <button id='chat-close-btn'>&times;</button>");
        out.println("    </div>");
        out.println("    <div id='chat-widget-body'></div>");
        out.println("    <div id='chat-widget-footer'>");
        out.println("      <input type='text' id='chat-input-field' placeholder='Type a message...' autocomplete='off' />");
        out.println("      <button id='chat-send-btn'><i class='fa-solid fa-paper-plane'></i></button>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</div>");

        /* WEBSOCKET INITIALIZER SCRIPT */
        out.println("<script>");
        out.println("  const wsUsername = '" + request.getSession().getAttribute("username") + "';");
        out.println("  const wsRole = '" + request.getSession().getAttribute("role") + "';");
        out.println("  const contextPath = '" + request.getContextPath() + "';");
        out.println("</script>");
        out.println("<script src='" + request.getContextPath() + "/js/websocket.js'></script>");

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
        Set<Class<?>> actionClasses = ClassScanner.scanForAction("app.action");
        
        // Sort them if needed, but for now just iterate
        for (Class<?> clazz : actionClasses) {
            Action action = clazz.getAnnotation(Action.class);
            if (action != null && action.showLink()) {
                // Role Check
                String requiredRoles = action.role();
                if (requiredRoles != null && !"ALL".equalsIgnoreCase(requiredRoles) &&
                        !"USER".equalsIgnoreCase(requiredRoles)) {
                    boolean hasPermission = false;
                    if (role != null) {
                        for (String r : requiredRoles.split(",")) {
                            if (r.trim().equalsIgnoreCase(role)) {
                                hasPermission = true;
                                break;
                            }
                        }
                    }
                    if (!hasPermission) {
                        continue;
                    }
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
