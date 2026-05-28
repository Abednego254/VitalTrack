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

        String requestPath = req.getPathInfo();
        String httpMethod = req.getMethod();

        ActionMapMatch actionMapMatch = ActionRegistry.findMatch(requestPath, httpMethod);

        if (actionMapMatch == null) {
            resp.sendError(404);
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
            Object actionCtxInstance = CDI.current()
                .select(actionMapMatch.getActionMap().getAction())
                .get();

            Object[] argsParams = ActionParamBinder.bind(actionMapMatch.getActionMap(), 
            req,resp,
            actionMapMatch.getPathVariables());


            // Actually call the method and get back the response.
            ActionResponse actionResponse = (ActionResponse) actionMapMatch
                .getActionMap()
                .getMethod()
                .invoke(actionCtxInstance, argsParams);

            String displayContent;
            if (actionResponse.getResponseText() != null) {
                displayContent = actionResponse.getResponseText();
                if (displayContent.startsWith("forward:")) {
                    req.getRequestDispatcher(
                            displayContent.substring(8)).forward(req, resp);
                    return;
                }

            } else {
                displayContent = vitalTrackFramework.htmlTable(actionResponse.getResponseClazz(),
                    actionResponse.getResponseDataList());
            }

            appPage.display(req, resp, displayContent);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}