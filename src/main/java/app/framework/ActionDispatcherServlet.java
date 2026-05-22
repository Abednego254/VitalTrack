package app.framework;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/vital/*")
public class ActionDispatcherServlet extends HttpServlet {

    @Inject
    private VitalTrackFramework vitalTrackFramework;

    @Inject
    private AppPage appPage;

    @Override
    public void init() {
        ActionRegistry.scanAndRegister("app.action");
    }

    // We override service() because it catches every type of HTTP method (GET, POST, DELETE, etc.)
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        // Look up the address book. Who should get this letter?
        String requestPath = req.getPathInfo();
        String httpMethod = req.getMethod();

        ActionMapMatch actionMapMatch = ActionRegistry.findMatch(requestPath, httpMethod);

        if (actionMapMatch == null) {
            resp.sendError(404); // No one lives here. Return to sender
            return;
        }

        Class<?> actionClass = actionMapMatch.getActionMap().getAction();
        Action actionAnnotation = actionClass.getAnnotation(Action.class);

        // Role Checking
        if (actionAnnotation != null && !"ALL".equalsIgnoreCase(actionAnnotation.role()) && !"USER"
        .equalsIgnoreCase(actionAnnotation.role())) {
            HttpSession session = req.getSession(false);
            String userRole = (session != null) ? (String) session.getAttribute("role") : null;
            String requiredRoles = actionAnnotation.role();
            boolean hasPermission = false;
            if (userRole != null) {
                for (String role : requiredRoles.split(",")) {
                    if (role.trim().equalsIgnoreCase(userRole)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
            if (!hasPermission) {
                resp.sendError(403, "Access Denied: Required Role(s): " + requiredRoles);
                return;
            }
        }

        try {
            // Go get the actual CDI-managed instance of the action class (not a plain new, a proper injected one).
            Object actionCtxInstance = CDI.current()
                .select(actionMapMatch.getActionMap().getAction())
                .get();

            // Open the letter and pull out all the form data / path variables — the arguments the method needs.
            Object[] argsParams = ActionParamBinder.bind(actionMapMatch.getActionMap(), 
            req,resp, // the HTTP request/response
            actionMapMatch.getPathVariables()); // any {id} variables captured above


            // Actually call the method and get back the response.
            ActionResponse actionResponse = (ActionResponse) actionMapMatch
                .getActionMap() // which method
                .getMethod()
                // It executes the method found in your Action class (like save() or list()) and receives an ActionResponse.
                .invoke(actionCtxInstance, argsParams);

            String displayContent;
            // If the response says forward:/some.jsp, pass the letter to that JSP page to render the HTML.
            if (actionResponse.getResponseText() != null) {
                displayContent = actionResponse.getResponseText();
                if (displayContent.startsWith("forward:")) {
                    req.getRequestDispatcher(
                            displayContent.substring(8)).forward(req, resp);
                    return;
                }

            } else {
                // If it returned a list of Java database objects (like a List<HospitalEquipment>),
                // delegate to our framework rendering engine to build the HTML table dynamically:
                displayContent = vitalTrackFramework.htmlTable(actionResponse.getResponseClazz(),
                    actionResponse.getResponseDataList());
            }

            appPage.display(req, resp, displayContent);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}